package com.example.ratest.presentation.components.layouts.ar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ratest.presentation.viewmodels.ARViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ratest.R
import com.example.ratest.presentation.components.layouts.CustomDialog
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.Green

@Composable
fun ModelPickerDialog(
    showModelPicker: Boolean,
    onDismissRequest: () -> Unit,
    viewModel: ARViewModel,
    showTapMessage: MutableState<Boolean>
) {
    if (showModelPicker) {
        var selectedModelPath by remember { mutableStateOf<String?>(null) }
        var selectedScale by remember { mutableFloatStateOf(1f) }
        val sampleImages: List<Pair<Int, String>> = listOf(
            R.drawable.helado to "models/helado.glb",
            R.drawable.monumento_fray_ia to "models/Cartel_Monumento_Fray.glb",
            R.drawable.monumento_madre_ai to "models/Cartel_Monumento_Madre.glb",
            R.drawable.palacio_municipal to "models/Cartel_Monumento_Palacio.glb",
            R.drawable.iglesia_ai to "models/Cartel_Iglesia.glb",
            R.drawable.coliseo_ai to "models/Cartel_Coliseo.glb",
        )
        CustomDialog(
            onDismissRequest = {
                onDismissRequest()
                selectedModelPath = null
            },
            title = "Selecciona un modelo 3D",
            content = {
                Column {
                    LazyRow(modifier = Modifier.padding(8.dp)) {
                        items(sampleImages) { item ->
                            val imageRes = item.first
                            val modelPath = item.second

                            Image(
                                painter = rememberAsyncImagePainter(imageRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(99.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedModelPath = modelPath
                                    }
                                    .border(
                                        width = if (selectedModelPath == modelPath) 2.dp else 0.dp,
                                        color = if (selectedModelPath == modelPath) DarkGreen else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tamaño: ${String.format("%.1f", selectedScale)}x",
                        color = Green,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Slider(
                        value = selectedScale,
                        onValueChange = { selectedScale = it },
                        valueRange = 0.6f..15f,
                        steps = 8,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            selectedModelPath?.let {
                                viewModel.selectedModelPath.value = it
                                viewModel.scaleModel.floatValue = selectedScale
                                showTapMessage.value = true
                                viewModel.arSceneView?.planeRenderer?.isVisible = true
                                onDismissRequest()
                            }
                        },
                        enabled = selectedModelPath != null,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        )
    }
}
