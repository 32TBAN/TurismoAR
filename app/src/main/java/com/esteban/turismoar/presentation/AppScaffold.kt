package com.esteban.turismoar.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.esteban.turismoar.R
import com.esteban.turismoar.presentation.components.layouts.CustomBottomBar
import com.esteban.turismoar.presentation.components.layouts.CustomLateralPlane
import com.esteban.turismoar.presentation.components.layouts.CustomTopBar
import com.esteban.turismoar.ui.theme.Green
import com.esteban.turismoar.ui.theme.White

@Composable
fun AppScaffold(
    navController: NavHostController,
    selectedIndex: Int,
    isBarsVisible: Boolean,
    onTabSelected: (Any) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomLateralPlane(navController)
        }
    ) {
    Scaffold(
        topBar = {
            AnimatedVisibility(modifier = Modifier.border(1.dp, Green.copy(alpha = 0.2f)) ,visible = isBarsVisible) {
                CustomTopBar(navController, scope, drawerState)
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
                    .background(White.copy(alpha = 0.2f))
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.collage_gastron_mico_salcedo),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .alpha(0.1f),
//                    contentScale = ContentScale.Crop
//                )
                content(innerPadding)
            }
        }
    )
    }
}
