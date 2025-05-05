package com.example.ratest.presentation.components.layouts.ar

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.ratest.R

@Composable
fun PlayTourSound() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.tour_start)
        mediaPlayer.start()
    }
}
