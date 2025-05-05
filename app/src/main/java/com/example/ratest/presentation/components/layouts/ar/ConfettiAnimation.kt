package com.example.ratest.presentation.components.layouts.ar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ratest.R
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.*

@Composable
fun ConfettiAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition,
        { progress },
        modifier = Modifier.fillMaxSize()
    )
}
