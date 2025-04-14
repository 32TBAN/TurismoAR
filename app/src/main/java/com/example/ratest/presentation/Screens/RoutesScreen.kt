package com.example.ratest.presentation.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.Utils.getCards
import com.example.ratest.presentation.Components.layouts.SectionCards

@Composable
fun RoutesScreen(navController: NavController) {
    val (cardsRutes) = getCards()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Explora las Rutas Turísticas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            SectionCards(
                cards = cardsRutes,
                tipe = "Info",
                scrollDirection = "vertical",
                navController = navController
            )
        }
    }
}



