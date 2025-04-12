package com.example.ratest.presentation.Components.layouts.Home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ratest.presentation.Components.layouts.SmallCard
import com.example.ratest.presentation.Components.models.CustomButton
import com.example.ratest.presentation.Components.models.SectionTitle
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.White
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


@Composable
fun TopSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(title = "¡Descubre Salcedo!", color = DarkGreen)
        Text(
            "Tus lugares favoritos, en tus manos. ",
            fontSize = 14.sp,
            color = DarkGreen
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

        CustomButton(text = "Comienza tu aventura", onClick = { /* TODO: Acción del botón */ })
    }
}

@Composable
fun MapSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Mapa de Salcedo",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier )
        Spacer(modifier = Modifier.height(50.dp))
        AndroidView(modifier = Modifier.fillMaxWidth().height(250.dp), factory = { context ->
            val mapview = MapView(context)
            mapview.setTileSource(TileSourceFactory.MAPNIK)
            mapview.setBuiltInZoomControls(true)
            mapview.setMultiTouchControls(true)
            mapview.controller.setZoom(14.0)
            mapview.controller.setCenter(GeoPoint(-1.04559, -78.59019))
            mapview
        })
    }


}

