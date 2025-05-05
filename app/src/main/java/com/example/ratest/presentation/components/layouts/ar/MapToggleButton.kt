package com.example.ratest.presentation.components.layouts.ar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.FloatingActionButton

@Composable
fun MapToggleButton(
    isMapVisible: Boolean,
    onToggle: () -> Unit
) {
    FloatingActionButton(
        onClick = onToggle,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(
            imageVector = if (isMapVisible) Icons.Default.Close else Icons.Default.Map,
            contentDescription = "Ver Mapa"
        )
    }
}
