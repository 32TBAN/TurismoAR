package com.example.ratest.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ratest.R
import com.example.ratest.presentation.components.layouts.CheckLocationPermission
import com.example.ratest.presentation.components.layouts.CustomBottomBar
import com.example.ratest.presentation.components.layouts.CustomTopBar
import com.example.ratest.presentation.components.layouts.DetalleInfo
import com.example.ratest.presentation.screens.ARScreen
import com.example.ratest.presentation.viewmodels.RouteViewModel
import com.example.ratest.ui.theme.White

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableStateOf(0) }
    var isTopBarVisible by remember { mutableStateOf(true) }
    var isBottomBarVisible by remember { mutableStateOf(true) }

    // LazyListState para detectar el desplazamiento
    val listState = rememberLazyListState()
    // Detectar el scroll y actualizar las barras
    LaunchedEffect(remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
        // Si el primer ítem visible tiene un índice mayor a 0, eso significa que estamos haciendo scroll hacia abajo
        if (listState.firstVisibleItemIndex > 0) {
            // Si la dirección es hacia abajo, ocultar las barras
            isTopBarVisible = false
            isBottomBarVisible = false
        } else {
            // Si no, mantener las barras visibles
            isTopBarVisible = true
            isBottomBarVisible = true
        }
    }

    val context = LocalContext.current
    val viewModel: RouteViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.initialize(context)
    }


    Scaffold(
        topBar = {
            AnimatedVisibility(
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 1,
                        easing = LinearEasing
                    )
                ),
                visible = isTopBarVisible,
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 11,
                        easing = LinearEasing
                    )
                )
            ) {
                CustomTopBar(navController = navController)
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 700,
                        easing = LinearEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 700,
                        easing = LinearEasing
                    )
                )
            ) {
                CustomBottomBar(selectedIndex = selectedIndex, onTabSelected = { screen ->
                    selectedIndex = screens.indexOf(screen)
                    navController.navigate(screen)
                })
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .border(width = .2.dp, color = Color.White)
                .background( White.copy(alpha = 0.9f)
                    // Para el gradiente
//                    Brush.linearGradient(
//                        colors = listOf(
//                            Blue.copy(alpha = 0.2f),
//                            LightGreen.copy(alpha = 0.4f),
//                            LightGreen.copy(alpha = 0.6f),
//                        ),
//                        start = Offset(400f, 400f),
//                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
//                    )
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.collage_gastron_mico_salcedo),
                contentDescription = "Fondo",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.1f),
                contentScale = ContentScale.Crop
            )
            NavHost(
                navController = navController,
                startDestination = InicioScreen
            ) {
                composable<InicioScreen> {
                    com.example.ratest.presentation.screens.InicioScreen(
                        navController,
                        listState,
                        viewModel
                    )
                }
                composable<RoutesScreen> {
                    com.example.ratest.presentation.screens.RoutesScreen(
                        navController,
                        listState,
                        viewModel
                    )
                }
                composable<HistoryScreen> {
                    com.example.ratest.presentation.screens.HistoryScreen(
                        navController,
                        listState,
                        viewModel
                    )
                }
                composable<RARScreen> {
                    val route = viewModel.selectedRoute.value

                    var isPermissionGranted by remember { mutableStateOf(false) }

                    CheckLocationPermission(
                        onGranted = {
                            isPermissionGranted = true
                        },
                        onDenied = {
                            isPermissionGranted = false
                        }
                    )

                    if (isPermissionGranted && route != null) {
                        isTopBarVisible = false
                        isBottomBarVisible = false
                        ARScreen(navController, route.geoPoints, route.type)
                    } else {
                        Text("Se requiere permiso de ubicación para continuar")
                    }
                }
                composable<DetalleScreen> {
                    DetalleInfo(navController, listState,viewModel)
                }
            }
        }

    }
}



