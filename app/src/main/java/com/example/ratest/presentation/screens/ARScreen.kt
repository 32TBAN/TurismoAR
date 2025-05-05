package com.example.ratest.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.presentation.components.layouts.ar.ARSceneContent
import com.example.ratest.presentation.components.layouts.LoadingScreen
import com.example.ratest.presentation.components.layouts.MapSection
import com.example.ratest.presentation.components.layouts.ar.CompassOverlay
import com.example.ratest.presentation.components.layouts.ar.ConfettiAnimation
import com.example.ratest.presentation.components.layouts.ar.MapIntroAnimation
import com.example.ratest.presentation.components.layouts.ar.MapToggleButton
import com.example.ratest.presentation.components.layouts.ar.PlayTourSound
import com.example.ratest.presentation.components.layouts.ar.ProgressOverlay
import com.example.ratest.presentation.components.models.BottomOverlay
import com.example.ratest.presentation.viewmodels.TourUIState
import com.example.ratest.ui.theme.Green

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
    var isMapVisible by remember { mutableStateOf(false) }
    var showMapIntro by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        // Se Inicializa el ViewModel con los geoPoints
        viewModel.initialize(context, validGeoPoints)
    }

    if (showMapIntro) {
        MapIntroAnimation(
            geoPoints = geoPoints,
            onFinish = { showMapIntro = false }
        )
        return
    }

    ARSceneContent(engine, viewModel, type)

    when (uiState) {
        is TourUIState.Loading -> {
            LoadingScreen(text = "Cargando destino...")
        }

        is TourUIState.InProgress -> {
            Column(modifier = Modifier.fillMaxSize()) {
                ProgressOverlay(
                    current = viewModel.getZisedVisitedPoints(),
                    total = validGeoPoints.size
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    CompassOverlay(
                        Modifier.align(Alignment.TopEnd)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    MapToggleButton(
                        isMapVisible = isMapVisible,
                        onToggle = { isMapVisible = !isMapVisible },
                        Modifier.align(Alignment.TopEnd)
                    )
                }

                Box(modifier = Modifier.weight(7f)) {
                    BottomOverlay(
                        distanceText = distanceText,
                        Modifier.align(Alignment.BottomCenter)
                    )
                }

                if (isMapVisible) {
                    Box(modifier = Modifier.weight(5f)) {
                        MapSection(
                            title = {
                                Text(
                                    "Mapa de recorrido",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Green
                                )
                            },
                            geoPoints = geoPoints,
                            type = "ruta",
                            zoomLevel = 20.0
                        )
                    }

                }
            }
        }

        is TourUIState.Arrived -> {
            val target = (uiState as TourUIState.Arrived).target
//            Log.d("GeoAR", "Arrived at: $target")
            PlayTourSound()
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
            PlayTourSound()
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
