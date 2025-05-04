package com.example.ratest.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.presentation.components.models.CustomButton
import com.example.ratest.presentation.viewmodels.ARViewModel
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.Green

@Composable
fun TourResultScreen(
    navController: NavController,
    totalPlaces: Int,
    visitedPlaces: Int,
    viewModel: ARViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎉 ¡Tour Completado!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Green
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Visitaste $visitedPlaces de $totalPlaces lugares turísticos.",
            fontSize = 20.sp,
            color = DarkGreen
        )

        Spacer(modifier = Modifier.height(24.dp))
        CustomButton("Comenzar de nuevo", onClick = { viewModel.restartTour() }, modifier = Modifier.padding(top = 32.dp))
        CustomButton("Volver al inicio", onClick = { navController.popBackStack() }, modifier = Modifier.padding(top = 10.dp))
    }
}
