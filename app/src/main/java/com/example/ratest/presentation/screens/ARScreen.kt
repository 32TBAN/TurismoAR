package com.example.ratest.presentation.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratest.presentation.viewmodels.ARViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.presentation.components.layouts.CustomDialog
import com.example.ratest.presentation.components.layouts.ar.ARSceneContent
import com.example.ratest.presentation.components.layouts.LoadingScreen
import com.example.ratest.presentation.components.layouts.MapSection
import com.example.ratest.presentation.components.layouts.ar.CompassOverlay
import com.example.ratest.presentation.components.layouts.ar.MapIntroAnimation
import com.example.ratest.presentation.components.layouts.ar.MapToggleButton
import com.example.ratest.presentation.components.layouts.ar.PlayTourSound
import com.example.ratest.presentation.components.layouts.ar.ProgressOverlay
import com.example.ratest.presentation.components.layouts.ar.TutorialDialog
import com.example.ratest.presentation.components.models.BottomOverlay
import com.example.ratest.presentation.viewmodels.TourUIState
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White
import com.example.ratest.presentation.components.layouts.ar.ModelPickerDialog
import com.example.ratest.R
import com.example.ratest.presentation.components.ErrorHandler
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun ARScreen(
    navController: NavController,
    geoPoints: List<GeoPoint>,
    type: String = "route"
) {
    val viewModel: ARViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsState()
    val distanceText by viewModel.distanceText.collectAsState()
    val context = LocalContext.current
    var validGeoPoints = geoPoints.filter { it.name != "" }
    var isMapVisible by remember { mutableStateOf(false) }
    var showMapIntro by remember { mutableStateOf(true) }
    val showTutorial = remember { mutableStateOf(false) }
    var showModelPicker by remember { mutableStateOf(false) }
    var showTapMessage by remember { mutableStateOf(false) }
    var isShowingMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Se Inicializa el ViewModel con los geoPoints
        viewModel.initialize(context, validGeoPoints)
    }

    val pickGalleryLauncher = rememberLauncherForActivityResult(
        contract = GetContent()
    ) { uri ->
        uri?.let {
            viewModel.imageUriState.value = it
        }
    }

    viewModel.imageUriState.value?.let { uri ->
        CustomDialog(
            onDismissRequest = { viewModel.imageUriState.value = null },
            confirmButton = {
                Button(onClick = { viewModel.imageUriState.value = null }) {
                    Text("Cerrar", color = Color.White)
                }
            },
            secondButtonContent = {
                Button(
                    onClick = {
                        viewModel.saveImageToGallery(context, uri)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Descargar", color = Color.White)
                }
            },
            content = {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        )
    }


    if (showMapIntro) {
        MapIntroAnimation(
            geoPoints = geoPoints,
            onFinish = { showMapIntro = false }
        )
        return
    }

    ARSceneContent(
        viewModel,
        type,
        navController
    )

    ModelPickerDialog(
        showModelPicker, {
            showModelPicker = false
            showTapMessage = true
        },
        viewModel,
        showTapMessage = mutableStateOf(showTapMessage)
    )

    if (showTapMessage && !isShowingMessage) {
        TutorialDialog(
            onDismiss = {
                showTapMessage = false
                isShowingMessage = true
            },
            pages = viewModel.pagesObjectTutorial,
            title = "Coloca el objeto"
        )
    }

    when (uiState) {
        is TourUIState.Loading -> {
            LoadingScreen(text = "Cargando destino...")
        }

        is TourUIState.InProgress -> {
            Column(modifier = Modifier.fillMaxSize()) {
                ProgressOverlay(
                    current = (viewModel.getZisedVisitedPoints()),
                    total = validGeoPoints.size
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    Box(modifier = Modifier.align(Alignment.TopStart)) {
                        FloatingActionButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(40.dp),
                            containerColor = Green
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Undo,
                                contentDescription = "Volver",
                                tint = White
                            )
                        }
                    }
                    MapToggleButton(
                        isMapVisible = isMapVisible,
                        onToggle = { isMapVisible = !isMapVisible },
                        Modifier.align(Alignment.TopEnd)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    Box(modifier = Modifier.align(Alignment.TopStart)) {
                        FloatingActionButton(
                            onClick = { showTutorial.value = true },
                            modifier = Modifier.size(40.dp),
                            containerColor = Green
                        ) {
                            Icon(
                                imageVector = Icons.Default.QuestionMark,
                                contentDescription = "Ayuda",
                                tint = White
                            )
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.TopEnd)) {
                        FloatingActionButton(
                            onClick = { showModelPicker = true },
                            modifier = Modifier.size(40.dp),
                            containerColor = Green
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                                contentDescription = "Elegir modelo",
                                tint = White
                            )
                        }

                    }
                }

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

                Box(modifier = Modifier.weight(7f)) {
                    BottomOverlay(
                        distanceText = distanceText,
                        Modifier.align(Alignment.BottomCenter),
                        onOpenGallery = { pickGalleryLauncher.launch("image/*") },
                        viewModel = viewModel
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
                            geoPoints = listOf(viewModel.currentPosition.value) as List<GeoPoint>,
                            type = "ruta",
                            zoomLevel = 20.0,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                if (showTutorial.value) {
                    TutorialDialog(
                        onDismiss = { showTutorial.value = false },
                        pages = viewModel.tutorialPages,
                        title = "¿Cómo usar el modo Realidad Aumentada?"
                    )
                }

            }
        }

        is TourUIState.Arrived -> {
            val target = (uiState as TourUIState.Arrived).target
//            Log.d("GeoAR", "Arrived at: $target")
            PlayTourSound(context)
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

//            CustomDialog(
//                title = "¡Llegaste a ${target.name}!",
//                onDismissRequest = { /* no dismiss automático */ },
//                confirmButtonText = "Marcar como visitado",
//                onConfirm = {
//                    viewModel.markCurrentTargetVisited()
//                }
//            ) {
//                Text("¿Quieres marcar este lugar como visitado?", color = Color.DarkGray)
//            }

        }

        is TourUIState.Completed -> {
            PlayTourSound(context, R.raw.complete)
            TourResultScreen(
                navController,
                validGeoPoints.size,
                viewModel.getZisedVisitedPoints(),
                viewModel
            )
        }

        is TourUIState.Error -> {
            ErrorHandler(errorState = viewModel.errorState, navController = navController)
        }
    }
}
