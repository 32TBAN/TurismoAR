package com.example.ratest.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.presentation.components.layouts.CardType
import com.example.ratest.presentation.components.layouts.ScrollDirection
import com.example.ratest.presentation.components.layouts.SectionCards
import com.example.ratest.presentation.mappers.toUiRoutes
import com.example.ratest.presentation.navigation.DetalleScreen
import com.example.ratest.presentation.viewmodels.RouteViewModel

@Composable
fun RoutesScreen(
    navController: NavController,
    viewModel: RouteViewModel
) {
    val context = LocalContext.current
    val tourRoutes by viewModel.tourRoutes.collectAsState()
    val uiRoutes = tourRoutes.toUiRoutes(context)

    Box(
        modifier = Modifier
            .fillMaxSize().padding(horizontal = 17.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SectionCards(
                routes = uiRoutes,
                cardType = CardType.Info,
                scrollDirection = ScrollDirection.Vertical,
                onRouteClick = { uiRoute ->
                    viewModel.selectRouteById(uiRoute.id)
                    navController.navigate(DetalleScreen(uiRoute.id))
                }
            )
        }
    }
}



