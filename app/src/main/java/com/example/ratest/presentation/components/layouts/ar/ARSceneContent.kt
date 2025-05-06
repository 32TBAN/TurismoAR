package com.example.ratest.presentation.components.layouts.ar

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ratest.presentation.viewmodels.ARViewModel
import com.google.android.filament.Engine
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberView

@Composable
fun ARSceneContent(
    engine: Engine,
    viewModel: ARViewModel,
    type: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val modelLoader = rememberModelLoader(engine = engine)
        val materialLoader = rememberMaterialLoader(engine = engine)
        val cameraNode = rememberARCameraNode(engine = engine)
        val view = rememberView(engine = engine)
        val childNodes = rememberNodes {
            add(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance("models/flecha.glb"),
                    scaleToUnits = 0.05f
                )
            )
        }
        val collisionSystem = rememberCollisionSystem(view = view)
        val planeRenderer = remember {
            mutableStateOf(false)
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
                try {
                    frame.value = updatedFrame
                    val earth = session.earth

                    viewModel.updateTarget(
                        earth?.cameraGeospatialPose?.latitude,
                        earth?.cameraGeospatialPose?.longitude
                    )

                    viewModel.updateSession(
                        updatedFrame, earth,
                        { anchorNode ->
                            if (anchorNode != null) {
                                childNodes.add(anchorNode)
                            } else {
                                if (childNodes.size > 1) {
                                    childNodes.removeAt(1)
                                }
                            }
                        },
                        modelInstance, engine, modelLoader, materialLoader, type
                    )

                    viewModel.updateArrowNode(
                        arrowNode = childNodes[0] as ModelNode,
                        frame = updatedFrame,
                        pinNode = if (childNodes.size > 1) childNodes[1] as? AnchorNode else null,
                        earth = earth
                    )
                } catch (e: Exception) {
                    Log.e("ARScreen", "Error updating session: ${e.message}")
                }
            },
            sessionConfiguration = { session, config ->
                config.geospatialMode = Config.GeospatialMode.ENABLED
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        else -> Config.DepthMode.DISABLED
                    }
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                session.configure(config)
            }
        )
    }
}
