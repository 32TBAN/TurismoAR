package com.esteban.turismoar.presentation.components.layouts.ar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esteban.turismoar.ui.theme.White
import androidx.compose.material3.Text
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import com.esteban.turismoar.presentation.viewmodels.ar.ARViewModel
import com.esteban.turismoar.ui.theme.LightGreen

@Composable
fun BottomOverlay(distanceText: String, onOpenGallery: () -> Unit, viewModel: ARViewModel) {
    val context = LocalContext.current
    val semiCircleShape = GenericShape { size, _ ->
        // semicírculo inferior
        moveTo(0f, size.height)
        arcTo(
            rect = Rect(
                0f, 0f, size.width, size.height * 2
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        close()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(semiCircleShape)
                .background(LightGreen.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onOpenGallery() }, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Abrir galería",
                        tint = White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Button(
                    onClick = { viewModel.onTakeScreenshot(context) },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).size(64.dp).border(1.dp, White, CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Tomar foto",
                        tint = White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    ){
                    Text(text = "Distancia", fontSize = 16.sp, color = White)
                    Text(
                        text = distanceText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

            }
        }
    }
}
