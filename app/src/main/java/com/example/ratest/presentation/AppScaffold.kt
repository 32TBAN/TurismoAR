package com.example.ratest.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.ratest.R
import com.example.ratest.presentation.components.layouts.CustomBottomBar
import com.example.ratest.presentation.components.layouts.CustomTopBar
import com.example.ratest.ui.theme.White

@Composable
fun AppScaffold(
    navController: NavHostController,
    selectedIndex: Int,
    isBarsVisible: Boolean,
    onTabSelected: (Any) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            AnimatedVisibility(visible = isBarsVisible) {
                CustomTopBar(navController)
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = isBarsVisible) {
                CustomBottomBar(
                    selectedIndex = selectedIndex,
                    onTabSelected = onTabSelected
                )
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(White.copy(alpha = 0.9f))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.collage_gastron_mico_salcedo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.1f),
                    contentScale = ContentScale.Crop
                )
                content(innerPadding)
            }
        }
    )
}
