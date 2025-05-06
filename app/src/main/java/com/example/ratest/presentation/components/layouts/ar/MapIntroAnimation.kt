package com.example.ratest.presentation.components.layouts.ar

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.presentation.components.layouts.MapSection
import kotlinx.coroutines.delay

@Composable
fun MapIntroAnimation(geoPoints: List<GeoPoint>, onFinish: () -> Unit) {
    var targetZoom by remember { mutableStateOf(3f) }
    val animatedZoom by animateFloatAsState(
        targetValue = targetZoom,
        animationSpec = tween(durationMillis = 800),
        label = "ZoomAnimation"
    )

    var showMap by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        repeat(6) {
            targetZoom += 2.5f
            delay(700)
        }
        delay(500)
        showMap = false
        onFinish()
    }

    if (showMap) {
        Box(modifier = Modifier.fillMaxSize()) {
            MapSection(
                title = {
                    Text(
                        text = "Ubicando destino...",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                geoPoints = geoPoints,
                zoomLevel = animatedZoom.toDouble(),
                controls = false,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}
