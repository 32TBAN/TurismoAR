package com.example.ratest.presentation.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.R
import com.example.ratest.Utils.getCards
import com.example.ratest.presentation.Components.layouts.Home.TopSection
import com.example.ratest.presentation.Components.layouts.MapSection
import com.example.ratest.presentation.Components.layouts.SectionCards

@Composable
fun InicioScreen(navController: NavController, listState: LazyListState = rememberLazyListState()) {
    val cardLists = getCards()

    LazyColumn(state = listState) {
        item {
            TopSection(navController)
        }

        itemsIndexed(cardLists) { index, cards ->
            val title = when (index) {
                0 -> stringResource(id = R.string.text_title_tourist_routes)
                1 -> stringResource(id = R.string.text_title_places_of_interest)
                2 -> stringResource(id = R.string.text_title_events)
                else -> "Otros"
            }

            SectionCards(
                title = title,
                cards = cards,
                navController = navController
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            MapSection()
        }
    }

}
