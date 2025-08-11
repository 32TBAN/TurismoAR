package com.esteban.turismoar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.esteban.turismoar.presentation.navigation.MainNavHost
import com.esteban.turismoar.presentation.navigation.screens
import com.esteban.turismoar.presentation.viewmodels.home.RouteViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppContent() {
    val navController = rememberNavController()
    val viewModel: RouteViewModel = koinViewModel()
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isBarsVisible by remember { mutableStateOf(true) }

    AppScaffold(
        navController = navController,
        selectedIndex = selectedIndex,
        isBarsVisible = isBarsVisible,
        onTabSelected = { route ->
            selectedIndex = screens.indexOf(route)
            navController.navigate(route)
        }
    ) {
        MainNavHost(
            navController = navController,
            viewModel = viewModel,
            setBarsVisible = { isBarsVisible = it }
        )
    }
}
