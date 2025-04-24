package com.example.ratest.presentation.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.Utils.Utils
import com.google.android.filament.utils.Quaternion
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Pose
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.NotTrackingException
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberView

@SuppressLint("DefaultLocale")
@Composable
fun ARScreen(
    navController: NavController,
    model: String
) {    //todo: dependiendo de la ruta enviar todas las coordenadas
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine = engine)
    val materialLoader = rememberMaterialLoader(engine = engine)
    val cameraNode = rememberARCameraNode(engine = engine)
    val childNodes = rememberNodes {
        add(
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/arrow.glb"
                ),
                scaleToUnits = 0.2f
            )
        )
    }
    val view = rememberView(engine = engine)
    val collisionSystem = rememberCollisionSystem(view = view)
    val planeRenderer = remember {
        mutableStateOf(true)
    }
    val modelInstance = remember {
        mutableListOf<ModelInstance>()
    }
    val trackingFailureReason = remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    val frame = remember {
        mutableStateOf<Frame?>(null)
    }
//    val targetLatLng = Pair(-1.016257, -78.565127)
    val targetLatLng = Pair(-1.016239, -78.565150)
    val distanceText = remember { mutableStateOf("Distancia: 0.0m") }
    val coordsText = remember { mutableStateOf("0,0") }
    val visibleRange = 10.0
    val isPinCreated = remember { mutableStateOf(false) }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        childNodes = childNodes,
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        collisionSystem = collisionSystem,
        planeRenderer = planeRenderer.value,
        cameraNode = cameraNode,
        materialLoader = materialLoader,
        onTrackingFailureChanged = {
            trackingFailureReason.value = it
        },
        onSessionUpdated = { session, updatedFrame ->
            frame.value = updatedFrame
            val earth = session.earth
            val cameraPose = updatedFrame.camera.pose

            try {
                if (earth?.trackingState == TrackingState.TRACKING) {
                    val geoPose = earth.cameraGeospatialPose

                    val distance = Utils.haversineDistance(
                        geoPose.latitude,
                        geoPose.longitude,
                        targetLatLng.first,
                        targetLatLng.second
                    )
//                    Log.d("GeoAR", "Distance: $distance meters")
                    distanceText.value = "Distancia: ${String.format("%.2f", distance)} m"
                    coordsText.value = " ${geoPose.latitude}, ${geoPose.longitude}"

                    if (distance <= visibleRange && !isPinCreated.value) {
                        val anchor = earth.createAnchor(
                            targetLatLng.first,
                            targetLatLng.second,
                            geoPose.altitude,
                            floatArrayOf(0f, 0f, 0f, 1f)
                        )

                        val adjustedTargetPosition = Float3(
                            targetLatLng.first.toFloat(),
                            targetLatLng.second.toFloat(),
                            geoPose.altitude.toFloat()
                        )

                        val pinNode = ModelNode(
                            modelInstance = modelLoader.createModelInstance(
                                assetFileLocation = "models/navigation_pin.glb"
                            ),
                            scaleToUnits = 0.5f
                        )

                        val anchorNode = AnchorNode(engine = engine, anchor = anchor).apply {
                            position = adjustedTargetPosition
                        }

                        pinNode.apply {
                            this.addChildNode(anchorNode)
                        }

                        childNodes.add(pinNode)
                        isPinCreated.value = true
                    }else if (distance > visibleRange && isPinCreated.value) {
                        childNodes.removeAt(1)
                        isPinCreated.value = false
                    }

                    cameraPose?.let {
                        val cameraPosition = Float3(it.tx(), it.ty(), it.tz())
                        val forwardDirection = Utils.quaternionToForward(it.rotationQuaternion)
                        val targetPosition = Float3(
                            targetLatLng.first.toFloat(),
                            targetLatLng.second.toFloat(),
                            geoPose.altitude.toFloat()
                        )

                        val directionToTarget = targetPosition - cameraPosition

                        childNodes[0].apply {
                            val distance = -0.3f
                            this.position = Float3(
                                it.tx() + forwardDirection.x * distance,
                                it.ty() - 0.2f,
                                it.tz() + forwardDirection.z * distance
                            )

                            val rotationQuaternion = FloatArray(4)
                            Utils.rotationBetweenVectors(
                                Float3(0f, 0f, 1f),
                                directionToTarget,
                                rotationQuaternion
                            )
                            this.rotation = Utils.quaternionToEulerAngles(
                                Quaternion(
                                    rotationQuaternion[0],
                                    rotationQuaternion[1],
                                    rotationQuaternion[2],
                                    rotationQuaternion[3]
                                )
                            )
                        }
                    }

                }
            } catch (e: NotTrackingException) {
                Log.e("GeoAR", "ARCore not tracking: ${e.message}")
                // Inform the user or try to recover
            } catch (e: Exception) {
                Log.e("GeoAR", "An unexpected error occurred: ${e.message}")
            }

        },
        sessionConfiguration = { session, config ->
            config.geospatialMode = Config.GeospatialMode.ENABLED
            config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                true -> Config.DepthMode.AUTOMATIC
                else -> Config.DepthMode.DISABLED
            }
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            session.configure(config)
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = distanceText.value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = coordsText.value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
