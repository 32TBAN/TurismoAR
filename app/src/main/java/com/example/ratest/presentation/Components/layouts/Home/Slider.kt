package com.example.ratest.presentation.Components.layouts.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageSlider(images: List<Int>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        items(images) { imageRes ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .aspectRatio(2.7f)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Imagen",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
