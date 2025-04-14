package com.example.ratest.presentation.Components.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.Utils.getCards
import com.example.ratest.presentation.Components.models.CustomButton
import com.example.ratest.presentation.Navigation.RARScreen
import com.example.ratest.ui.theme.DarkGreen

@Composable
fun DetalleInfo(routeId: String, navController: NavController) {
    val place = getCards().flatMap { it }.find { it.title == routeId }

    place?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Image(
                painter = painterResource(id = it.imageRes),
                contentDescription = it.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = it.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = it.description,
                style = MaterialTheme.typography.body2,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            MapSection(title = "Ruta", geoPoints = it.geoPoints, zoomLevel = 17.0, tipe = it.tipe)

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = "Regresar",
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Explorar con Realidad Aumentada",
                onClick = {
                    navController.navigate(RARScreen(it.rute))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
