package com.example.ratest.presentation.screens.ar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.presentation.components.layouts.ar.ConfettiAnimation
import com.example.ratest.presentation.components.buttons.CustomButton
import com.example.ratest.presentation.viewmodels.ar.ARViewModel

@Composable
fun TourResultScreen(
    navController: NavController,
    totalPlaces: Int,
    visitedPlaces: Int,
    viewModel: ARViewModel
) {
    ConfettiAnimation()
    AlertDialog(
        onDismissRequest = { /* No se puede cerrar tocando fuera */ },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Imagen superior decorativa
//                Image(
//                    painter = painterResource(id = R.drawable.tour_completed),
//                    contentDescription = "Tour completado",
//                    modifier = Modifier
//                        .height(150.dp)
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    contentScale = ContentScale.Fit
//                )

                Text(
                    text = "🎉 ¡Tour Completado!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = "Visitaste $visitedPlaces de $totalPlaces lugares turísticos.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomButton(
                    text = "Reiniciar",
                    onClick = { viewModel.restartTour() },
                    modifier = Modifier.weight(1f)
                )
                CustomButton(
                    text = "Inicio",
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        iconContentColor = MaterialTheme.colorScheme.primary
    )
}

