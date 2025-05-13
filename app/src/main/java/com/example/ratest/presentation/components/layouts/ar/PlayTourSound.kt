package com.example.ratest.presentation.components.layouts.ar

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import com.example.ratest.R

@Composable
fun PlayTourSound(context: Context, soundResId: Int = R.raw.tour_start) {
    val mediaPlayer = MediaPlayer.create(context, soundResId)
    mediaPlayer.setOnCompletionListener {
        it.release()
    }
    mediaPlayer.start()
}
