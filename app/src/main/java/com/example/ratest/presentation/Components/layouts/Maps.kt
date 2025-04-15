package com.example.ratest.presentation.Components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapSection(
    title: String = "Mapa de Salcedo",
    geoPoints: List<Triple<Double, Double, String>> = emptyList(),
    zoomLevel: Double = 12.7,
    controls: Boolean = true,
    tipe: String = "ruta"
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.height(50.dp))
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp), factory = { context ->
                val userAgent = "com.example.ratest/1.0"
                Configuration.getInstance().userAgentValue = userAgent
                val mapview = MapView(context)
                mapview.setTileSource(TileSourceFactory.MAPNIK)
                mapview.setBuiltInZoomControls(controls)
                mapview.setMultiTouchControls(controls)
                mapview.controller.setZoom(zoomLevel)

                if (!geoPoints.isEmpty()) {
                    mapview.controller.setCenter(GeoPoint(geoPoints[0].first, geoPoints[0].second))

                    if (tipe == "marcador") {
                        geoPoints.forEach { (lat, lon, nombre) ->
                            if (!nombre.isEmpty()) {
                                val marker = Marker(mapview)

                                marker.position = GeoPoint(lat, lon)
                                marker.title = nombre
                                mapview.overlays.add(marker)
                            }
                        }
                    }

                    val polyline = Polyline()
                    geoPoints.forEach { (lat, lon, nombre) ->
                        polyline.addPoint(GeoPoint(lat, lon))
                    }

                    polyline.setColor(Color.Blue.hashCode())
                    polyline.width = 18f

                    mapview.overlays.add(polyline)

                    geoPoints.forEach { (lat, lon, nombre) ->
                        if (!nombre.isEmpty()) {
                            val marker = Marker(mapview)

                            marker.position = GeoPoint(lat, lon)
                            marker.title = nombre
                            mapview.overlays.add(marker)
                        }
                    }
                } else {
                    val defaultPoint = GeoPoint(-1.04559, -78.59019)
                    mapview.controller.setCenter(defaultPoint)
                    val marker = Marker(mapview)

                    marker.position = defaultPoint
                    marker.title = "Salcedo"
                    mapview.overlays.add(marker)
                }
                mapview
            })
    }
}