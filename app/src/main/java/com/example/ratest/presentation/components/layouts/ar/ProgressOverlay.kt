package com.example.ratest.presentation.components.layouts.ar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip

@Composable
fun ProgressOverlay(current: Int, total: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto de progreso
        Text(
            text = "Progreso: $current / $total",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Línea de puntos con animación
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(total) { index ->
                val targetColor =
                    if (index < current) Color.White else Color.Gray.copy(alpha = 0.4f)
                val animatedColor by animateColorAsState(
                    targetValue = targetColor,
                    animationSpec = tween(durationMillis = 300)
                )

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(animatedColor)
                )
            }
        }
    }
}
