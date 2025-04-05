package com.example.ratest.presentation.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.presentation.Navigation.RARScreen

@Composable
fun RoutesScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            Button(onClick = {
                // Pasar el modelo al ARScreen al hacer clic
                navController.navigate(RARScreen("1"))
            }) {
                Text("Ruta 1")
            }
            Button(onClick = {
                // Pasar el modelo al ARScreen al hacer clic
                navController.navigate(RARScreen("2"))
            }) {
                Text("Ruta 2")
            }
            Button(onClick = {
                // Pasar el modelo al ARScreen al hacer clic
                navController.navigate(RARScreen("3"))
            }) {
                Text("Ruta 3")
            }
            Button(onClick = {
                // Pasar el modelo al ARScreen al hacer clic
                navController.navigate(RARScreen("4"))
            }) {
                Text("Ruta 4")
            }
            Button(onClick = {
                // Pasar el modelo al ARScreen al hacer clic
                navController.navigate(RARScreen("5"))
            }) {
                Text("Ruta 5")
            }
        }
    }
}


