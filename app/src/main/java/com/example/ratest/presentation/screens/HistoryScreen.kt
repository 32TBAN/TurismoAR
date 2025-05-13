package com.example.ratest.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.presentation.components.layouts.CardBackgroundImage
import com.example.ratest.presentation.mappers.UiRoute
import com.example.ratest.presentation.navigation.DetalleScreen
import com.example.ratest.presentation.mappers.toUiRoutes
import com.example.ratest.presentation.viewmodels.RouteViewModel


@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: RouteViewModel
) {
    val context = LocalContext.current
    val visitedRoutes by viewModel.brandRoutes.collectAsState()
    val uiRoutes = visitedRoutes.toUiRoutes(context)

    HistoryContent(
        uiRoutes = uiRoutes,
        onRouteClick = { route ->
            viewModel.selectRouteById(route.id)
            navController.navigate(DetalleScreen(route.id))
        }
    )
}

@Composable
fun HistoryContent(
    uiRoutes: List<UiRoute>,
    onRouteClick: (UiRoute) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 17.dp)) {

        if (uiRoutes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no has visitado ningún lugar.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn {
                items(uiRoutes) { route ->
                    CardBackgroundImage(
                        title = route.title,
                        description = route.description,
                        imageRes = route.imageRes,
                        onClick = { onRouteClick(route) }
                    )
                }
            }
        }
    }
}
