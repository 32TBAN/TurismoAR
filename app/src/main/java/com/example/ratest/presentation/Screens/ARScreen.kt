package com.example.ratest.presentation.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ratest.Utils.GeoPoint
import com.example.ratest.Utils.Utils
import com.example.ratest.presentation.viewmodels.TourUIState
import io.github.sceneview.ar.node.AnchorNode

@Composable
fun ARScreen(
    navController: NavController,
    geoPoints: List<GeoPoint>
) {
    val viewModel: ARViewModel = viewModel()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        var validGeoPoints = geoPoints.filter { it.name != "" }
        viewModel.initialize(context, validGeoPoints)
    }
    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    val currentTarget by viewModel.currentTarget.collectAsState()

    when (uiState) {
        is TourUIState.Loading -> {
            Text("Cargando tour...", color = Color.White)
        }

        is TourUIState.InProgress -> {
            val distanceText by viewModel.distanceText.collectAsState()
            val target = (uiState as TourUIState.InProgress).target

            val engine = rememberEngine()
            val modelLoader = rememberModelLoader(engine = engine)
            val materialLoader = rememberMaterialLoader(engine = engine)
            val cameraNode = rememberARCameraNode(engine = engine)

            val arrowNode = remember {
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(
                        assetFileLocation = "models/arrow.glb"
                    ),
                    scaleToUnits = 0.2f
                )
            }

            val childNodes = rememberNodes {
                add(arrowNode)
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

                    if (earth != null) {
                        val geoPose = earth.cameraGeospatialPose

                        viewModel.updateTarget(geoPose.latitude, geoPose.longitude)

                        currentTarget?.let { target ->
                            val distance = Utils.haversineDistance(
                                geoPose.latitude,
                                geoPose.longitude,
                                target.latitude,
                                target.longitude
                            )

                            if (distance <= 2 && !showDialog) {
                                showDialog = true
                            }
                        }

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
                            modelInstance, engine, modelLoader, materialLoader
                        )

                        viewModel.updateArrowNode(
                            arrowNode = childNodes[0] as ModelNode,
                            frame = updatedFrame,
                            pinNode = if (childNodes.size > 1) childNodes[1] as? AnchorNode else null,
                            earth = earth
                        )
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
            Text("Se dirije a ${target.name}", color = Color.White)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = distanceText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        is TourUIState.Arrived -> {
            val target = (uiState as TourUIState.Arrived).target
            Log.d("ARScreen", "Llegaste a ${target.name}")
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "¡Llegaste a ${currentTarget?.name}!") },
                text = { Text("¿Quieres marcar este lugar como visitado?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.markCurrentTargetVisited()
                            showDialog = false
                        }
                    ) {
                        Text("Sí")
                    }
                }
            )
        }

        is TourUIState.Completed -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎉 ¡Felicidades!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
                Text(
                    text = "Completaste el tour turístico.",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color.White
                )
            }
        }

        is TourUIState.Error -> {
            val message = (uiState as TourUIState.Error).message
            Text("Error: $message", color = Color.Red)
        }
    }
}
