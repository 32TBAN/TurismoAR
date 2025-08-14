package com.esteban.turismoar.presentation.components.layouts.ar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.presentation.components.layouts.map.MapSection
import kotlinx.coroutines.delay

@Composable
fun MapIntroAnimation(geoPoints: List<GeoPoint>, onFinish: () -> Unit) {
    var targetZoom by remember { mutableFloatStateOf(3f) }
    val animatedZoom by animateFloatAsState(
        targetValue = targetZoom,
        animationSpec = tween(durationMillis = 800),
        label = "ZoomAnimation"
    )

    var showMap by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        repeat(6) {
            targetZoom += 2.5f
            delay(300)
        }
        delay(700)
        showMap = false
        onFinish()
    }

    if (showMap) {
        Box(modifier = Modifier.fillMaxSize()) {
            MapSection(
                title =  "Ubicando destino...",
                geoPoints = geoPoints,
                zoomLevel = animatedZoom.toDouble(),
                controls = false,
                modifierMap = Modifier.fillMaxSize()
            )
        }
    }

}
