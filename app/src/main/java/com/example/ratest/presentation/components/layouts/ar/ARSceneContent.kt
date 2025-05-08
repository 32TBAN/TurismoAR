package com.example.ratest.presentation.components.layouts.ar

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ratest.presentation.viewmodels.ARViewModel
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node

@Composable
fun ARSceneContent(
    viewModel: ARViewModel,
    type: String
) {

    val trackingFailureReason = remember { mutableStateOf<TrackingFailureReason?>(null) }
    val frame = remember { mutableStateOf<Frame?>(null) }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            ARSceneView(ctx).apply {
                val engine = this.engine
                val modelLoader = this.modelLoader
                val materialLoader = this.materialLoader
                viewModel.arSceneView = this

                val arrowNode = mutableListOf<Node>().apply {
                    add(
                        ModelNode(
                            modelInstance = modelLoader.createModelInstance("models/flecha.glb"),
                            scaleToUnits = 0.05f
                        )
                    )
                }

                this.addChildNodes(arrowNode)

                this.planeRenderer.isVisible = false

                this.sessionConfiguration = { session, config ->
                    config.geospatialMode = Config.GeospatialMode.ENABLED
                    config.depthMode =
                        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC))
                            Config.DepthMode.AUTOMATIC
                        else
                            Config.DepthMode.DISABLED

                    config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                    config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                    session.configure(config)
                }

                this.onSessionUpdated = { session, updatedFrame ->
                    try {
                        frame.value = updatedFrame
                        val earth = session.earth

                        viewModel.updateTarget(
                            earth?.cameraGeospatialPose?.latitude,
                            earth?.cameraGeospatialPose?.longitude
                        )

                        viewModel.updateSession(
                            updatedFrame, earth,
                            engine, modelLoader, materialLoader, type
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
                }

                this.onTrackingFailureChanged = {
                    trackingFailureReason.value = it
                }
            }
        }
    )
}
