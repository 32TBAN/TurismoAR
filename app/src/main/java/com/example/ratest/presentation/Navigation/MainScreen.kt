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
import com.example.ratest.presentation.Navigation.NavGraph
import com.example.ratest.presentation.Navigation.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(painterResource(id = R.drawable.ic_route), contentDescription = "Rutas") },
                    label = { Text("Rutas") },
                    selected = navController.currentDestination?.route == Screen.Rutas.route,
                    onClick = { navController.navigate(Screen.Rutas.route) }
                )
                BottomNavigationItem(
                    icon = { Icon(painterResource(id = R.drawable.ic_home), contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = navController.currentDestination?.route == Screen.Inicio.route,
                    onClick = { navController.navigate(Screen.Inicio.route) }
                )
                BottomNavigationItem(
                    icon = { Icon(painterResource(id = R.drawable.ic_history), contentDescription = "Historia") },
                    label = { Text("Historia") },
                    selected = navController.currentDestination?.route == Screen.Historia.route,
                    onClick = { navController.navigate(Screen.Historia.route) }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController)
        }
    }
}

