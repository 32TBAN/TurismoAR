package com.example.ratest.presentation.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ratest.R

@Preview(
    showBackground = true
)
@Composable
fun InicioScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(text = "Bienvenido", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(20.dp))

        // Descripción
        Text(
            text = "Descubre las rutas turísticas, conoce la historia local y vive una experiencia única en el cantón Salcedo.",
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Botones de navegación
        Button(
            onClick = {  onNavigate("rutas") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Explorar Rutas")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onNavigate("historia") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Conocer Historia")
        }

        Spacer(modifier = Modifier.height(40.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(5) { index ->
                Image(
                    painter = painterResource(id = R.drawable.iglesia),
                    contentDescription = "Imagen destacada",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.4f)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.monumento),
                    contentDescription = "Imagen destacada",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.4f)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.mercado),
                    contentDescription = "Imagen destacada",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.4f)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.palacio_municipal),
                    contentDescription = "Imagen destacada",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.4f)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.parque),
                    contentDescription = "Imagen destacada",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.4f)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
