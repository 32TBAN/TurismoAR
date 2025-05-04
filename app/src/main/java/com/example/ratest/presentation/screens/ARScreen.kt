package com.example.ratest.presentation.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberView
import com.example.ratest.presentation.viewmodels.ARViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.presentation.Components.models.BottomOverlay
import com.example.ratest.presentation.viewmodels.TourUIState
import io.github.sceneview.ar.node.AnchorNode

@Composable
fun ARScreen(
    navController: NavController,
    geoPoints: List<GeoPoint>,
    modelName: String = "pin",
    type: String = "route"
) {
    val viewModel: ARViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val distanceText by viewModel.distanceText.collectAsState()

    val context = LocalContext.current
    var validGeoPoints = geoPoints.filter { it.name != "" }

    LaunchedEffect(Unit) {
        // Se Inicializa el ViewModel con los geoPoints
        viewModel.initialize(context, validGeoPoints)
    }

    when (uiState) {
        is TourUIState.Loading -> {
            Text("Cargando tour...", color = Color.White)
        }

        is TourUIState.InProgress -> {
            val engine = rememberEngine()
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

            Box(modifier = Modifier.fillMaxSize()) {
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
                            modelInstance, engine, modelLoader, materialLoader, modelName, type
                        )

                        viewModel.updateArrowNode(
                            arrowNode = childNodes[0] as ModelNode,
                            frame = updatedFrame,
                            pinNode = if (childNodes.size > 1) childNodes[1] as? AnchorNode else null,
                            earth = earth
                        )

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

                BottomOverlay(distanceText = distanceText)
            }
        }

        is TourUIState.Arrived -> {
            val target = (uiState as TourUIState.Arrived).target
            Log.d("GeoAR", "Arrived at: $target")
            AlertDialog(
                onDismissRequest = {
                    uiState !is TourUIState.Arrived
                },
                title = { Text(text = "¡Llegaste a ${target.name}!") },
                text = { Text("¿Quieres marcar este lugar como visitado?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.markCurrentTargetVisited()
                        }
                    ) {
                        Text("Sí")
                    }
                }
            )
        }

        is TourUIState.Completed -> {
            TourResultScreen(
                navController,
                validGeoPoints.size,
                viewModel.getZisedVisitedPoints(),
                viewModel
            )
        }

        is TourUIState.Error -> {
            val message = (uiState as TourUIState.Error).message
            Text("Error: $message", color = Color.Red)
        }
    }
}
