package com.example.ratest.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.R
import com.example.ratest.presentation.components.layouts.home.TopSection
import com.example.ratest.presentation.components.layouts.MapSection
import com.example.ratest.presentation.components.layouts.SectionCards
import com.example.ratest.presentation.viewmodels.RouteViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.example.ratest.presentation.components.layouts.CardType
import com.example.ratest.presentation.components.layouts.ScrollDirection
import com.example.ratest.presentation.mappers.UiRoute
import com.example.ratest.presentation.mappers.toUiRoutes
import com.example.ratest.presentation.navigation.DetalleScreen
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: RouteViewModel
) {
    val context = LocalContext.current
    val allRoutes by viewModel.allRoutes.collectAsState()
    val uiRoutes = allRoutes.toUiRoutes(context)

    val groupedRoutes = remember(uiRoutes) {
        listOf(
            "ruta" to uiRoutes.filter { it.type == "ruta" },
            "marca" to uiRoutes.filter { it.type == "marca" },
            "evento" to uiRoutes.filter { it.type == "evento" }
        )
    }

    var showFullMap by remember { mutableStateOf(false) }

    HomeContent(
        groupedRoutes = groupedRoutes,
        navController = navController,
        viewModel = viewModel,
        onMapButtonClick = { showFullMap = true }
    )

    if (showFullMap) {
        FullScreenMapOverlay(onClose = { showFullMap = false })
    }
}

@Composable
fun HomeContent(
    groupedRoutes: List<Pair<String, List<UiRoute>>>,
    navController: NavController,
    viewModel: RouteViewModel,
    onMapButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 17.dp)
    ) {
        LazyColumn {
            item { TopSection(navController) }

            itemsIndexed(groupedRoutes) { _, (type, routes) ->
                val title = when (type) {
                    "ruta" -> stringResource(id = R.string.text_title_tourist_routes)
                    "marca" -> stringResource(id = R.string.text_title_places_of_interest)
                    "evento" -> stringResource(id = R.string.text_title_events)
                    else -> "Otros"
                }

                GroupedRoutesSection(
                    title = title,
                    routes = routes,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            MiniMapButton(onClick = onMapButtonClick)
        }
    }
}

@Composable
fun GroupedRoutesSection(
    title: String,
    routes: List<UiRoute>,
    navController: NavController,
    viewModel: RouteViewModel
) {
    SectionCards(
        title = title,
        routes = routes,
        cardType = CardType.Small,
        scrollDirection = ScrollDirection.Horizontal,
        onRouteClick = { uiRoute ->
            viewModel.selectRouteById(uiRoute.id)
            navController.navigate(DetalleScreen(uiRoute.id))
        }
    )
}

@Composable
fun FullScreenMapOverlay(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        MapSection(
            modifier = Modifier.fillMaxSize(),
            type = "ruta"
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cerrar Mapa",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .size(36.dp)
                .clickable { onClose() }
        )
    }
}


@Composable
fun MiniMapButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(50.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Green.copy(alpha = 0.8f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Map,
            contentDescription = "Ver Mapa",
            tint = White
        )
    }
}
