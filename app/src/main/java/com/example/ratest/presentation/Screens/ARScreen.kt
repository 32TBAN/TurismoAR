package com.example.ratest.presentation.Screens

import android.view.MotionEvent
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
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingFailureReason
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
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
    var arrowNode = rememberNode(engine = engine)
    val isMarkerCreated = remember { mutableStateOf(false) }
    val isArrowCreated = remember { mutableStateOf(false) }

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
        onSessionUpdated = { _, updatedFrame ->
            frame.value = updatedFrame
            val cameraPose = updatedFrame?.camera?.pose

            if (isArrowCreated.value == false) {
                updatedFrame.getUpdatedPlanes()
                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                    ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                        var arrowAnchor = Utils.createAnchorNode(
                            engine = engine,
                            modelLoader = modelLoader,
                            materialLoader = materialLoader,
                            modelInstance = modelInstance,
                            anchor = anchor,
                            model = Utils.getModel("arrow")
                        )
                        arrowNode = arrowAnchor
                        childNodes += arrowAnchor
                        isArrowCreated.value = true
                    }
            } else {
                cameraPose?.let {
                    val cameraPosition = Float3(it.tx(), it.ty(), it.tz())
                    val forwardDirection = Utils.quaternionToForward(it.rotationQuaternion)
                    val targetPosition = Float3(-1.016257f, -78.565153f, 20.71f)

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
                        val eulerAngles = Utils.quaternionToEulerAngles(
                            Quaternion(
                                rotationQuaternion[0],
                                rotationQuaternion[1],
                                rotationQuaternion[2],
                                rotationQuaternion[3]
                            )
                        )
                        this.rotation = eulerAngles

                        if (!isMarkerCreated.value) {
                            val targetPose = Pose(
                                floatArrayOf(
                                    targetPosition.x,
                                    targetPosition.y,
                                    targetPosition.z
                                ), FloatArray(4) { 0f })

                            updatedFrame.getUpdatedPlanes()
                                .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                                ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                                    var targetAnchor = Utils.createAnchorNode(
                                        engine = engine,
                                        modelLoader = modelLoader,
                                        materialLoader = materialLoader,
                                        modelInstance = modelInstance,
                                        anchor = anchor,
                                        model = Utils.getModel("pin")
                                    )
                                    targetAnchor.position = targetPosition
                                    childNodes += targetAnchor
                                }
                            isMarkerCreated.value = true
                        }
                    }
                }
            }
        },
        sessionConfiguration = { session, config ->
            config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                true -> Config.DepthMode.AUTOMATIC
                else -> Config.DepthMode.DISABLED
            }
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        },
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { e: MotionEvent, node: Node? ->
                if (node == null) {
                    val hitTestResult = frame.value?.hitTest(e.x, e.y)
                    hitTestResult?.firstOrNull {
                        it.isValid(
                            depthPoint = false,
                            point = false
                        )
                    }?.createAnchorOrNull()?.let {
                        val nodeModel = Utils.createAnchorNode(
                            engine = engine,
                            modelLoader = modelLoader,
                            materialLoader = materialLoader,
                            modelInstance = modelInstance,
                            anchor = it,
                            model = Utils.getModel(model)
                        )
                        childNodes += nodeModel
                    }
                }
            }
        )
    )
}


