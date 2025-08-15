package com.esteban.turismoar.presentation.screens.ar

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.esteban.turismoar.presentation.viewmodels.ar.ARViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.presentation.components.layouts.ar.ARSceneContent
import com.esteban.turismoar.presentation.components.layouts.LoadingScreen
import com.esteban.turismoar.presentation.components.layouts.map.MapSection
import com.esteban.turismoar.presentation.components.layouts.ar.CompassOverlay
import com.esteban.turismoar.presentation.components.layouts.ar.MapIntroAnimation
import com.esteban.turismoar.presentation.components.layouts.ar.PlayTourSound
import com.esteban.turismoar.presentation.components.layouts.ar.ProgressOverlay
import com.esteban.turismoar.presentation.components.layouts.ar.TutorialDialog
import com.esteban.turismoar.presentation.components.layouts.ar.BottomOverlay
import com.esteban.turismoar.presentation.viewmodels.TourUIState
import com.esteban.turismoar.presentation.components.layouts.ar.ModelPickerDialog
import com.esteban.turismoar.presentation.components.ErrorHandler
import com.esteban.turismoar.presentation.components.layouts.ar.SelectedImageDialog
import com.esteban.turismoar.presentation.components.layouts.ar.TourArrivedDialog
import com.esteban.turismoar.presentation.components.buttons.ARFloatingButtons
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun ARScreen(
    navController: NavController,
    geoPoints: List<GeoPoint>,
    type: String = "route"
) {
    val viewModel: ARViewModel = koinViewModel()
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val distanceText by viewModel.distanceText.collectAsState()

    var validGeoPoints = geoPoints.filter { it.name != "" }
    var isMapVisible by remember { mutableStateOf(false) }
    var showMapIntro by remember { mutableStateOf(true) }
    val showTutorial = remember { mutableStateOf(false) }
    var showModelPicker by remember { mutableStateOf(false) }
    var showTapMessage by remember { mutableStateOf(false) }
    var isShowingMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.initialize(validGeoPoints)
    }

    val pickGalleryLauncher = rememberLauncherForActivityResult(
        contract = GetContent()
    ) { uri ->
        uri?.let {
            viewModel.imageUriState.value = it
        }
    }

    SelectedImageDialog(
        uri = viewModel.imageUriState.value,
        onClose = { viewModel.imageUriState.value = null },
        onDownload = {
            viewModel.imageUriState.value?.let {
                viewModel.saveImageToGallery(context, it)
            }
        }
    )

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
            texts = viewModel.pagesObjectTutorial,
            title = "Coloca el objeto",
            images = viewModel.imagesObjectTutorial
        )
    }

    when (uiState) {
        is TourUIState.Loading -> LoadingScreen(text = "Cargando destino...")
        is TourUIState.InProgress -> {
            Column(modifier = Modifier.fillMaxSize()) {
                ProgressOverlay(
                    current = (viewModel.getZisedVisitedPoints()),
                    total = validGeoPoints.size
                )

                ARFloatingButtons(
                    onBack = { navController.popBackStack() },
                    onHelp = { showTutorial.value = true },
                    onModelPicker = { showModelPicker = true },
                    isMapVisible = isMapVisible,
                    toggleMap = { isMapVisible = !isMapVisible }
                )

                CompassOverlay()

                Box(modifier = Modifier.weight(5f)){
                    BottomOverlay(
                        distanceText = distanceText,
                        onOpenGallery = { pickGalleryLauncher.launch("image/*") },
                        viewModel = viewModel
                    )
                }

                if (isMapVisible) {
                    Box(modifier = Modifier.weight(3f)) {
                        MapSection(
                            geoPoints = listOfNotNull(viewModel.currentPosition.value),
                            type = "marcador",
                            zoomLevel = 20.0,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                if (showTutorial.value) {
                    TutorialDialog(
                        onDismiss = { showTutorial.value = false },
                        texts = viewModel.tutorialPages,
                        title = "¿Cómo usar el modo Realidad Aumentada?",
                        images = viewModel.tutorialImages
                    )
                }

            }
        }

        is TourUIState.Arrived -> {
            val target = (uiState as TourUIState.Arrived).target
            PlayTourSound(context)
            TourArrivedDialog(
                targetName = target.name,
                onConfirm = { viewModel.markCurrentTargetVisited() },
                onDismiss = { }
            )
        }

        is TourUIState.Completed -> {
//            PlayTourSound(context, R.raw.complete)
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
