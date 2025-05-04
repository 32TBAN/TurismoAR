package com.example.ratest.presentation.components.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratest.ui.theme.White
import androidx.compose.material3.Text
import com.example.ratest.ui.theme.LightGreen

@Composable
fun BottomOverlay(distanceText: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val semiCircleShape = GenericShape { size, _ ->
            // semicírculo inferior
            moveTo(0f, size.height)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    0f, 0f, size.width, size.height * 2
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            close()
        }
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(120.dp)
                .clip(semiCircleShape)
                .background(LightGreen.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = distanceText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}
