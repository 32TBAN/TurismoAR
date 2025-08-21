package com.esteban.turismoar.presentation.components.buttons

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.esteban.turismoar.ui.theme.DarkGreen
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import com.esteban.turismoar.presentation.components.layouts.ar.MapToggleButton
import com.esteban.turismoar.ui.theme.Blue
import com.esteban.turismoar.ui.theme.Green
import com.esteban.turismoar.ui.theme.White
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import com.esteban.turismoar.ui.theme.LightGreen

enum class VariantButton { Solid, Bordered, Text }
enum class ColorButton {  Default, Primary, Secondary, Success, Warning, Danger }

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    variant: VariantButton = VariantButton.Solid,
    color: ColorButton = ColorButton.Default
) {
    // Colores de fondo según el tipo
    val colorFill = when (color) {
        ColorButton.Default -> White
        ColorButton.Primary -> Blue
        ColorButton.Secondary -> DarkGreen
        ColorButton.Success -> Green
        ColorButton.Warning -> Color.Yellow
        ColorButton.Danger -> Color.Red
    }

    // Colores de contenido (texto / icono)
    val colorContent = when (color) {
        ColorButton.Default -> DarkGreen
        else -> White
    }

    when (variant) {
        VariantButton.Solid -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorFill,
                    contentColor = colorContent
                )
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                }
                Text(text)
            }
        }

        VariantButton.Bordered -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colorFill),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorFill
                )
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                }
                Text(text)
            }
        }

        VariantButton.Text -> {
            TextButton(
                onClick = onClick,
                modifier = modifier,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorFill
                )
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                }
                Text(text)
            }
        }
    }
}

@Composable
fun ExploreARButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .wrapContentWidth().width(230.dp)
            .clip(RoundedCornerShape(50))
            .background(
                Brush.horizontalGradient(colors = listOf(Blue, Green))
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
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
    val showHelpHint = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FABAction(Icons.AutoMirrored.Filled.Undo, "Volver", onBack)
            MapToggleButton(isMapVisible = isMapVisible, onToggle = toggleMap)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                FABAction(Icons.Default.QuestionMark, "Ayuda") {
                    onHelp()
                    showHelpHint.value = false
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = showHelpHint.value, modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(start = 50.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "👈 Aprende cómo funciona",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF333333),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color(0xFF333333)
                        )
                    }
                }

            }

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
