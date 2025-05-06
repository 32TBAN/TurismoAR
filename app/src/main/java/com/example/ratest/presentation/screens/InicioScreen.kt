package com.example.ratest.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import com.example.ratest.presentation.mappers.toUiRoutes

@Composable
fun InicioScreen(
    navController: NavController,
    listState: LazyListState = rememberLazyListState(),
    viewModel: RouteViewModel
) {
    val context = LocalContext.current
    val allRoutes by viewModel.allRoutes.collectAsState()
    val uiRoutes = allRoutes.toUiRoutes(context)

    val groupedRoutes = listOf(
        "ruta" to uiRoutes.filter { it.type == "ruta" },
        "marca" to uiRoutes.filter { it.type == "marca" },
        "evento" to uiRoutes.filter { it.type == "evento" }
    )

    LazyColumn(state = listState) {
        item {
            TopSection(navController)
        }

        itemsIndexed(groupedRoutes) { index, (type, routes) ->
            val title = when (type) {
                "ruta" -> stringResource(id = R.string.text_title_tourist_routes)
                "marca" -> stringResource(id = R.string.text_title_places_of_interest)
                "evento" -> stringResource(id = R.string.text_title_events)
                else -> "Otros"
            }

            SectionCards(
                title = title,
                routes = routes,
                navController = navController,
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            MapSection(modifier = Modifier.height(200.dp), geoPoints = uiRoutes.flatMap { it.geoPoints }, type = "ruta")
        }
    }

}
