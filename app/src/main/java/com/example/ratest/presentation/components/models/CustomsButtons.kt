package com.example.ratest.presentation.components.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ratest.ui.theme.DarkGreen
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import com.example.ratest.presentation.components.layouts.ar.MapToggleButton
import com.example.ratest.ui.theme.Blue
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
    ) {
        icon?.let {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        }
        Text(text = text, color = Color.White)
    }
}

@Composable
fun ExploreARButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .width(230.dp)
            .clip(RoundedCornerShape(50))
            .background(
                Brush.horizontalGradient(colors = listOf(Blue, Green))
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Iniciar RA",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Explorar con Realidad Aumentada",
                color = White,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun ARFloatingButtons(
    onBack: () -> Unit,
    onHelp: () -> Unit,
    onModelPicker: () -> Unit,
    isMapVisible: Boolean,
    toggleMap: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth().padding(horizontal =  17.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical =  8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            FABAction(Icons.AutoMirrored.Filled.Undo, "Volver", onBack)
            MapToggleButton(isMapVisible = isMapVisible, onToggle = toggleMap)
        }

        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            FABAction(Icons.Default.QuestionMark, "Ayuda", onHelp)
            FABAction(Icons.AutoMirrored.Filled.QueueMusic, "Elegir modelo", onModelPicker)
        }
    }
}

@Composable
fun FABAction(icon: ImageVector, description: String, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
        containerColor = Green
    ) {
        Icon(icon, contentDescription = description, tint = White)
    }
}
