package com.example.ratest.presentation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.Utils.getCards
import com.example.ratest.presentation.Components.layouts.CardBackgroundImage


@Composable
fun HistoryScreen(navController: NavController) {
    val (cardsRutes, cardsPlaces) = getCards()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Historia Local",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn() {
            items(cardsPlaces) { card ->
                CardBackgroundImage(
                    title = card.title,
                    description = card.description,
                    imageRes = card.imageRes
                )
            }
        }
    }
}