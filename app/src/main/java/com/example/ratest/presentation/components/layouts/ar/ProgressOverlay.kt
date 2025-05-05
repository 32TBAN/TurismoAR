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

@Composable
fun ProgressOverlay(current: Int, total: Int) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text("Progreso: $current / $total", color = Color.White)
    }
}
