package com.esteban.turismoar.presentation.components.inputs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.esteban.turismoar.ui.theme.Blue
import com.esteban.turismoar.ui.theme.Green

@Composable
fun ImageSlider(images: List<Int>) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val currentIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(images.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxSize()
                        .aspectRatio(2.4f)
                ) {
                    Image(
                        painter = painterResource(id = images[index]),
                        contentDescription = "Imagen",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (currentIndex == index) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (currentIndex == index) Blue else Green
                        )
                )
            }
        }
    }
}
