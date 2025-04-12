package com.example.ratest.presentation.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.Utils.getCards
import com.example.ratest.presentation.Components.layouts.HistoricPlaceCard
import com.example.ratest.presentation.Components.layouts.Home.MapSection
import kotlin.collections.component1


@Composable
fun HistoryScreen(navController: NavController) {
    val (cardsPlaces) = getCards()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Historia Local",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Muestra las tarjetas de los lugares históricos
        LazyColumn {
            items(cardsPlaces) { place ->
                HistoricPlaceCard(
                    title = place.title,
                    description = place.description,
                    imageRes = place.imageRes,
                    onClick = {
                        //todo: Navegar a la pantalla de detalles del lugar histórico
                        navController.navigate("detalleHistoria/${place.title}")
                    }
                )
            }
        }
    }
}