package com.example.ratest.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratest.R
import com.example.ratest.Utils.getCards
import com.example.ratest.ui.theme.White
import com.example.ratest.presentation.Components.layouts.Home.TopSection
import com.example.ratest.presentation.Components.layouts.MapSection
import com.example.ratest.presentation.Components.layouts.SectionCards

@Composable
fun InicioScreen(navController: NavController) {
    val cardLists = getCards()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        TopSection(navController)
        cardLists.forEachIndexed { index, cards ->
            val title = when(index) {
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
        MapSection()
    }
}
