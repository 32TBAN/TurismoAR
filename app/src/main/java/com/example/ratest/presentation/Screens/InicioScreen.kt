package com.example.ratest.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.navigation.NavController
import com.example.ratest.presentation.Components.models.CustomButton
import com.example.ratest.presentation.Components.models.SectionTitle
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White

@Composable
fun InicioScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        TopSection()
        Spacer(modifier = Modifier.height(16.dp))
        TouristRoutesSection()
        Spacer(modifier = Modifier.height(16.dp))
        PlacesOfInterestSection()
    }
}

@Composable
fun TopSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x088408))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(title = "¡Descubre Salcedo!", color = Green)
        Text(
            "Tus puntos favoritos de Salcedo, en tus manos. ",
            fontSize = 14.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            //todo: cambiar imagen
            Text("Imagen de Salcedo", modifier = Modifier.align(Alignment.Center))
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(text = "boton 1", onClick = { /* TODO: Acción del botón */ })
    }
}

@Composable
fun TouristRoutesSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Tourist Routes", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            //todo: cambiar rutas
            RouteCard("Monuments")
            RouteCard("Museums")
        }
    }
}

@Composable
fun RouteCard(title: String) {
    Card(
        modifier = Modifier
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Place, contentDescription = null)
            Text(title)
        }
    }
}

@Composable
fun PlacesOfInterestSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Places of Interest", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InterestCard("Monuments")
            InterestCard("Churches")
        }
    }
}

@Composable
fun InterestCard(title: String) {
    Card(
        modifier = Modifier
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(title)
        }
    }
}
