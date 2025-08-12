package com.esteban.turismoar.presentation.navigation

import android.Manifest
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.esteban.turismoar.presentation.components.layouts.CustomDialog
import com.esteban.turismoar.presentation.screens.detail.DetailInfo
import com.esteban.turismoar.presentation.components.layouts.PermissionGate
import com.esteban.turismoar.presentation.screens.ar.ARScreen
import com.esteban.turismoar.presentation.screens.history.HistoryScreen
import com.esteban.turismoar.presentation.screens.home.HomeScreen
import com.esteban.turismoar.presentation.screens.route.RoutesScreen
import com.esteban.turismoar.presentation.screens.accions.AddScreen
import com.esteban.turismoar.presentation.viewmodels.home.RouteViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: RouteViewModel,
    setBarsVisible: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {
        composable<HomeScreen> {
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
        composable<DetailScreen> {
            setBarsVisible(true)
            DetailInfo(navController, viewModel)
        }
        composable<AddScreen> {
            setBarsVisible(false)
            AddScreen(navController)
        }
    }
}




