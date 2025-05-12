package com.example.ratest.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.presentation.components.layouts.CardBackgroundImage
import com.example.ratest.presentation.navigation.DetalleScreen
import com.example.ratest.presentation.mappers.toUiRoutes
import com.example.ratest.presentation.viewmodels.RouteViewModel


@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: RouteViewModel
) {
    val context = LocalContext.current
    val tourRoutes by viewModel.brandRoutes.collectAsState()
    val uiRoutes = tourRoutes.toUiRoutes(context)

    LazyColumn(modifier = Modifier.padding(horizontal = 17.dp)) {
        items(uiRoutes) { card ->
            CardBackgroundImage(
                title = card.title,
                description = card.description,
                imageRes = card.imageRes,
                onClick = { navController.navigate(DetalleScreen(card.id)) }
            )
        }
    }

}