package com.example.ratest.presentation.screens

import android.R.attr.delay
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.sceneview.rememberEngine
import com.example.ratest.presentation.viewmodels.ARViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.presentation.components.layouts.ar.ARSceneContent
import com.example.ratest.presentation.components.layouts.LoadingScreen
import com.example.ratest.presentation.components.models.BottomOverlay
import com.example.ratest.presentation.viewmodels.TourUIState

@Composable
fun ARScreen(
    navController: NavController,
    geoPoints: List<GeoPoint>,
    type: String = "route"
) {
    val viewModel: ARViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val distanceText by viewModel.distanceText.collectAsState()

    val context = LocalContext.current
    var validGeoPoints = geoPoints.filter { it.name != "" }

    val engine = rememberEngine()
    var showARScene by remember { mutableStateOf(true) }
    BackHandler(enabled = true) {
        showARScene = false
    }
    DisposableEffect(showARScene) {
        onDispose {
            if (!showARScene) {
                engine.destroy()
            }
        }
    }

    LaunchedEffect(Unit) {
        // Se Inicializa el ViewModel con los geoPoints
        viewModel.initialize(context, validGeoPoints)
    }
    ARSceneContent(engine, viewModel, type, onExitScene = { showARScene = false })
    when (uiState) {
        is TourUIState.Loading -> {
            LoadingScreen(text = "Cargando destino...")
        }

        is TourUIState.InProgress -> {
            Box(modifier = Modifier.fillMaxSize()) {
                BottomOverlay(distanceText = distanceText)
            }
        }

        is TourUIState.Arrived -> {
            val target = (uiState as TourUIState.Arrived).target
//            Log.d("GeoAR", "Arrived at: $target")
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
