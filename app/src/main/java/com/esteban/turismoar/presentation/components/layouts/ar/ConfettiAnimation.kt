package com.esteban.turismoar.presentation.components.layouts.ar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.*
import com.esteban.turismoar.R

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
