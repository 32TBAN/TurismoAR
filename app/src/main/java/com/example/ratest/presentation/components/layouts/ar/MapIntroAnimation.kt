package com.example.ratest.presentation.components.layouts.ar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun MapIntroAnimation(onFinish: () -> Unit) {
    // Aquí podrías usar Google Maps Compose con animaciones de cámara
    // Simular una cámara descendiendo hacia el destino

    // Simulación simple con delay
    LaunchedEffect(Unit) {
        delay(3000) // Tiempo para mostrar el mapa
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Text("Ubicando destino...", color = Color.White)
    }
}
