package com.example.ratest.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Scaffold
import com.example.ratest.R
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf("inicio") }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_route),
                            contentDescription = "Rutas"
                        )
                    },
                    label = { Text("Rutas") },
                    selected = selectedScreen == "rutas",
                    onClick = { selectedScreen = "rutas" }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_home),
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inico") },
                    selected = selectedScreen == "inicio",
                    onClick = { selectedScreen = "inicio" }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_history),
                            contentDescription = "Historia"
                        )
                    },
                    label = { Text("Historia") },
                    selected = selectedScreen == "historia",
                    onClick = { selectedScreen = "historia" }
                )

            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (selectedScreen) {
                "rutas" -> RoutesScreen()
                "historia" -> HistoryScreen()
                "inicio" -> InicioScreen()
            }
        }
    }
}