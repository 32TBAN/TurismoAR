package com.example.ratest.presentation.Screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ratest.Utils.Utils
import com.google.android.filament.utils.Quaternion
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Pose
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberView

@Composable
fun ARScreen(navController: NavController, model: String) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine = engine)
    val materialLoader = rememberMaterialLoader(engine = engine)
    val cameraNode = rememberARCameraNode(engine = engine)
    val childNodes = rememberNodes()
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

    val isMarkerCreated = remember { mutableStateOf(false) }

    var arrowNode = rememberNode(engine = engine)
    val isArrowCreated = remember { mutableStateOf(false) }
    val targetLatLng = Pair(-1.016257, -78.565127)

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
            val geoPose = earth?.cameraGeospatialPose
            val cameraPose = updatedFrame.camera.pose

            if (geoPose != null && earth.trackingState == TrackingState.TRACKING) {
                val distance = Utils.haversineDistance(
                    targetLatLng.first,
                    targetLatLng.second,
                    geoPose.altitude,
                    geoPose.longitude
                )
                Log.e("GeoAR","$distance")
                if (!isMarkerCreated.value) {
                    val anchor = earth.createAnchor(
                        targetLatLng.first,
                        targetLatLng.second,
                        geoPose.altitude,
                        floatArrayOf(0f, 0f, 0f, 1f)
                    )
                    var pinNode = Utils.createAnchorNode(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstance = modelInstance,
                        anchor = anchor,
                        model = Utils.getModel("pin")
                    )
                    childNodes += pinNode
                    isMarkerCreated.value = true
                }

                if (!isArrowCreated.value) {
                    var arrowAnchor = session.createAnchor(
                        cameraPose.compose(
                            Pose.makeTranslation(
                                0f,
                                -0.5f,
                                -1.5f
                            )
                        )
                    )
                    var arrowNodeInstance = Utils.createAnchorNode(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstance = modelInstance,
                        anchor = arrowAnchor,
                        model = Utils.getModel("arrow")
                    )
                    childNodes += arrowNodeInstance
                    arrowNode = arrowNodeInstance
                    isArrowCreated.value = true
                }

                if (isArrowCreated.value) {
                    cameraPose?.let {
                        val cameraPosition = Float3(it.tx(), it.ty(), it.tz())
                        val forwardDirection = Utils.quaternionToForward(it.rotationQuaternion)
                        val targetPosition = Float3(
                            targetLatLng.first.toFloat(),
                            targetLatLng.second.toFloat(),
                            geoPose.altitude.toFloat()
                        )

                        val directionToTarget = targetPosition - cameraPosition

                        arrowNode?.apply {
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
}
