package com.example.ratest.presentation.Screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.ratest.Utils.getCards
import com.example.ratest.presentation.Components.layouts.CardBackgroundImage
import com.example.ratest.presentation.Navigation.DetalleScreen


@Composable
fun HistoryScreen(
    navController: NavController,
    listState: LazyListState = rememberLazyListState()
) {
    val (cardsRutes, cardsPlaces) = getCards()

    LazyColumn(state = listState) {
        items(cardsPlaces) { card ->
            CardBackgroundImage(
                title = card.title,
                description = card.description,
                imageRes = card.imageRes,
                onClick = { navController.navigate(DetalleScreen(card.title)) }
            )
        }
    }

}