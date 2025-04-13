package com.example.ratest.presentation.Components.layouts.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.ratest.R
import com.example.ratest.Utils.getList
import com.example.ratest.presentation.Components.models.CustomButton
import com.example.ratest.presentation.Components.models.SectionTitle
import com.example.ratest.presentation.Navigation.RoutesScreen
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.White
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun TopSection(navController: NavController) {
    val images = getList()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(title = stringResource(R.string.text_title_section_home), color = DarkGreen)
        Text(
            stringResource(R.string.text_subtitle_home),
            fontSize = 14.sp,
            color = DarkGreen,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        ImageSlider(images = images)

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            text = stringResource(R.string.button_text_home1),
            onClick = { navController.navigate(RoutesScreen) })
    }
}

@Composable
fun MapSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Mapa de Salcedo",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(50.dp))
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp), factory = { context ->
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
