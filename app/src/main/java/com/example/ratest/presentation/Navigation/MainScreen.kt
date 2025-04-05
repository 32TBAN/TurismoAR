package com.example.ratest.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.ratest.R
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.ratest.presentation.Navigation.HistoryScreen
import com.example.ratest.presentation.Navigation.InicioScreen
import com.example.ratest.presentation.Navigation.RARScreen
import com.example.ratest.presentation.Navigation.RoutesScreen
import com.example.ratest.presentation.Screens.ARScreen
import com.example.ratest.presentation.Screens.HistoryScreen
import com.example.ratest.presentation.Screens.InicioScreen
import com.example.ratest.presentation.Screens.RoutesScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                // Botón para "Rutas"
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_route),
                            contentDescription = "Rutas"
                        )
                    },
                    label = { Text("Rutas") },
                    selected = navController.currentDestination?.route == "routes", // Comparar con la ruta de RoutesScreen
                    onClick = { navController.navigate(RoutesScreen) } // Navegar a RoutesScreen
                )

                // Botón para "Inicio"
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_home),
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") },
                    selected = navController.currentDestination?.route == "inicio", // Comparar con la ruta de InicioScreen
                    onClick = { navController.navigate(InicioScreen) } // Navegar a InicioScreen
                )

                // Botón para "Historia"
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_history),
                            contentDescription = "Historia"
                        )
                    },
                    label = { Text("Historia") },
                    selected = navController.currentDestination?.route == "history", // Comparar con la ruta de HistoryScreen
                    onClick = { navController.navigate(HistoryScreen) } // Navegar a HistoryScreen
                )
            }
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
                    HistoryScreen() // Composable para la pantalla de historia
                }
                composable<RARScreen> {
                    val model = it.toRoute<RARScreen>().model
                    ARScreen(navController,model)
                }
            }
        }

    }
}


