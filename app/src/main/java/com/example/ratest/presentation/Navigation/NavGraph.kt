package com.example.ratest.presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ratest.presentation.Screens.HistoryScreen
import com.example.ratest.presentation.Screens.InicioScreen
import com.example.ratest.presentation.Screens.RoutesScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route
    ) {
        composable(Screen.Rutas.route) { RoutesScreen() }
        composable(Screen.Historia.route) { HistoryScreen() }
        composable(Screen.Inicio.route) {
            InicioScreen(
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}
