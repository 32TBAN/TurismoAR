package com.example.ratest.presentation.components.layouts.ar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.unit.dp
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White

@Composable
fun MapToggleButton(
    isMapVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onToggle,
        modifier = modifier.size(40.dp),
        containerColor = Green
    ) {
        Icon(
            imageVector = if (isMapVisible) Icons.Default.Close else Icons.Default.Map,
            contentDescription = "Ver Mapa",
            tint = White
        )
    }
}
