package com.example.ratest.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.ratest.presentation.navigation.MainNavHost
import com.example.ratest.presentation.navigation.screens
import com.example.ratest.presentation.viewmodels.RouteViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppContent() {
    val navController = rememberNavController()
    val viewModel: RouteViewModel = koinViewModel()
    var selectedIndex by remember { mutableStateOf(0) }
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
