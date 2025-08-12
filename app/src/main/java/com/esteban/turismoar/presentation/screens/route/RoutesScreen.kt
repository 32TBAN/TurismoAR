package com.esteban.turismoar.presentation.screens.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.esteban.turismoar.presentation.components.layouts.CardType
import com.esteban.turismoar.presentation.components.layouts.ScrollDirection
import com.esteban.turismoar.presentation.components.layouts.SectionCards
import com.esteban.turismoar.presentation.mappers.toUiRoutes
import com.esteban.turismoar.presentation.navigation.DetailScreen
import com.esteban.turismoar.presentation.viewmodels.home.RouteViewModel

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
                    navController.navigate(DetailScreen(uiRoute.id))
                }
            )
        }
    }
}



