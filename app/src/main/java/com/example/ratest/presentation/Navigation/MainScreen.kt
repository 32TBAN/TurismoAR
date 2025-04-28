package com.example.ratest.presentation.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.ratest.R
import com.example.ratest.presentation.Components.layouts.CheckLocationPermission
import com.example.ratest.presentation.Components.layouts.CustomBottomBar
import com.example.ratest.presentation.Components.layouts.CustomTopBar
import com.example.ratest.presentation.Components.layouts.DetalleInfo
import com.example.ratest.presentation.Navigation.DetalleScreen
import com.example.ratest.presentation.Navigation.HistoryScreen
import com.example.ratest.presentation.Navigation.InicioScreen
import com.example.ratest.presentation.Navigation.RARScreen
import com.example.ratest.presentation.Navigation.RoutesScreen
import com.example.ratest.presentation.Navigation.screens
import com.example.ratest.presentation.Screens.ARScreen
import com.example.ratest.presentation.Screens.HistoryScreen
import com.example.ratest.presentation.Screens.InicioScreen
import com.example.ratest.presentation.Screens.RoutesScreen

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
    LaunchedEffect(listState.firstVisibleItemIndex) {
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
                    InicioScreen(navController, listState)
                }
                composable<RoutesScreen> {
                    RoutesScreen(navController, listState)
                }
                composable<HistoryScreen> {
                    HistoryScreen(navController, listState)
                }
                composable<RARScreen> {
//                    val model = it.toRoute<RARScreen>().model
                    val geoPoints = it.toRoute<RARScreen>().geoPoints
                    var isPermissionGranted by remember { mutableStateOf(false) }

                    CheckLocationPermission(
                        onGranted = {
                            isPermissionGranted = true
                        },
                        onDenied = {
                            isPermissionGranted = false
                        }
                    )

                    if (isPermissionGranted) {
                        isTopBarVisible = false
                        isBottomBarVisible = false
                        ARScreen(navController, geoPoints)
                    } else {
                        Text("Se requiere permiso de ubicación para continuar")
                    }
                }
                composable<DetalleScreen> {
                    val routeId = it.toRoute<DetalleScreen>().routeId
                    DetalleInfo(routeId, navController, listState)
                }
            }
        }

    }
}



