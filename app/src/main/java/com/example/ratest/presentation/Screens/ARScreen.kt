package com.example.ratest.presentation.Screens

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
import com.example.ratest.Utils.Utils
import io.github.sceneview.ar.node.AnchorNode

@Composable
fun ARScreen(
    navController: NavController,
    geoPoints: List<Triple<Double, Double, String>>
) {
    val viewModel: ARViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.setGeoPoints(geoPoints)
    }

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

    val targetLatLng = Pair(-1.016238, -78.565148)
    val distanceText by viewModel.distanceText.collectAsState()
    val currentTarget by viewModel.currentTarget.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

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

                viewModel.updateCurrentTarget(earth)

                currentTarget?.let { target ->
                    val distance = Utils.haversineDistance(
                        geoPose.latitude,
                        geoPose.longitude,
                        target.first,
                        target.second
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
                    targetLatLng = targetLatLng,
                    earth = earth
                )
            }
        },
        sessionConfiguration = { session, config ->
            config.geospatialMode = Config.GeospatialMode.ENABLED
            config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                true -> Config.DepthMode.AUTOMATIC
                else -> Config.DepthMode.DISABLED
            }
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
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
            text = distanceText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

    if (showDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "¡Llegaste a ${currentTarget?.third}!") },
            text = { Text("¿Quieres marcar este lugar como visitado?") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        viewModel.markCurrentTargetVisited()
                        //todo hacer esta funcion
                        showDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }

}
