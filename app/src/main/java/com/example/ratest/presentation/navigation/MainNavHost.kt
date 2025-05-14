package com.example.ratest.presentation.navigation

import android.Manifest
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ratest.presentation.components.layouts.CustomDialog
import com.example.ratest.presentation.screens.detail.DetailInfo
import com.example.ratest.presentation.components.layouts.PermissionGate
import com.example.ratest.presentation.screens.ar.ARScreen
import com.example.ratest.presentation.screens.history.HistoryScreen
import com.example.ratest.presentation.screens.home.HomeScreen
import com.example.ratest.presentation.screens.route.RoutesScreen
import com.example.ratest.presentation.viewmodels.home.RouteViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: RouteViewModel,
    setBarsVisible: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = InicioScreen
    ) {
        composable<InicioScreen> {
            setBarsVisible(true)
            HomeScreen(navController, viewModel)
        }
        composable<RoutesScreen> {
            setBarsVisible(true)
            RoutesScreen(navController, viewModel)
        }
        composable<HistoryScreen> {
            setBarsVisible(true)
            HistoryScreen(navController, viewModel)
        }
        composable<RARScreen> {
            val route = viewModel.selectedRoute.value
            PermissionGate(
                permission = Manifest.permission.ACCESS_FINE_LOCATION,
                onGranted = {
                    if (route != null) {
                        setBarsVisible(false)
                        ARScreen(navController, route.geoPoints, route.type)
                    }
                },
                onDenied = {
                    CustomDialog(
                        onDismissRequest = { navController.popBackStack() },
                        title = "Permiso de ubicación",
                        content = {
                            Text("Para poder utilizar la aplicación necesitas otorgar el permiso de ubicación")
                        },
                        confirmButton = {
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Aceptar", color = Color.White)
                            }
                        }
                    )
                }
            )
        }
        composable<DetalleScreen> {
            setBarsVisible(true)
            DetailInfo(navController, viewModel)
        }
    }
}




