package com.example.ratest.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.ratest.presentation.Components.layouts.CustomBottomBar
import com.example.ratest.presentation.Navigation.HistoryScreen
import com.example.ratest.presentation.Navigation.InicioScreen
import com.example.ratest.presentation.Navigation.RARScreen
import com.example.ratest.presentation.Navigation.RoutesScreen
import com.example.ratest.presentation.Navigation.screens
import com.example.ratest.presentation.Screens.ARScreen
import com.example.ratest.presentation.Screens.HistoryScreen
import com.example.ratest.presentation.Screens.InicioScreen
import com.example.ratest.presentation.Screens.RoutesScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            CustomBottomBar(selectedIndex = selectedIndex, onTabSelected = { screen ->
                selectedIndex = screens.indexOf(screen)
                navController.navigate(screen)
            })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = InicioScreen // Ruta de inicio
            ) {
                composable<InicioScreen> {
                    InicioScreen(navController) // Composable paara la pantalla de inicio
                }
                composable<RoutesScreen> {
                    RoutesScreen(navController) // Composable para la pantalla de rutas
                }
                composable<HistoryScreen> {
                    HistoryScreen(navController) // Composable para la pantalla de historia
                }
                composable<RARScreen> {
                    val model = it.toRoute<RARScreen>().model
                    ARScreen(navController, model)
                }
            }
        }

    }
}


