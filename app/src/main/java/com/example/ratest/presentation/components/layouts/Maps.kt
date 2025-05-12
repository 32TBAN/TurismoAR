package com.example.ratest.presentation.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ratest.domain.models.GeoPoint as GeoPointCustom


@Composable
fun MapSection(
    title: @Composable () -> Unit = {
        Text(
            "Mapa de Salcedo",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
    },
    geoPoints: List<GeoPointCustom> = emptyList(),
    zoomLevel: Double = 15.7,
    controls: Boolean = true,
    type: String = "ruta",
    modifier: Modifier
) {
    title()
    Spacer(modifier = Modifier.height(2.dp))
    Column() {
//        Spacer(modifier = Modifier.height(96.dp))
        AndroidView(
            modifier = modifier.clip(RoundedCornerShape(16.dp)),
            factory = { context ->
                val userAgent = "com.example.ratest/1.0"
                Configuration.getInstance().userAgentValue = userAgent
                val mapview = MapView(context)
                mapview.setTileSource(TileSourceFactory.MAPNIK)
                mapview.setBuiltInZoomControls(controls)
                mapview.setMultiTouchControls(controls)
                mapview.controller.setZoom(zoomLevel)
//                mapview.maxZoomLevel = 19.0
//                mapview.minZoomLevel = zoomLevel
//                mapview.setTilesScaledToDpi(true)
                if (geoPoints.isNotEmpty()) {
                    mapview.controller.setCenter(
                        GeoPoint(
                            geoPoints[0].latitude,
                            geoPoints[0].longitude
                        )
                    )
                    val osmdroidPoints = geoPoints.map { GeoPoint(it.latitude, it.longitude) }

                    val polyline = Polyline().apply {
                        setPoints(osmdroidPoints)
                        color = Color.Blue.hashCode()
                        width = 10f
                    }
                    mapview.overlays.add(polyline)

                    if (type == "marcador" || type == "ruta") {
                        geoPoints.forEach {
                            if (it.name.isNotEmpty()) {
                                val marker = Marker(mapview).apply {
                                    position = GeoPoint(it.latitude, it.longitude)
                                }
                                marker.title = it.name
                                mapview.overlays.add(marker)
                            }
                        }
                    }

                    geoPoints.forEach { (lat, lon, name) ->
                        if (!name.isEmpty()) {
                            val marker = Marker(mapview)

                            marker.position = GeoPoint(lat, lon)
                            marker.title = name
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
            },
            update = { mapView ->
                if (!geoPoints.isEmpty()) {
                    val controls = mapView.controller
                    controls.setZoom(zoomLevel)
                    controls.setCenter(
                        GeoPoint(
                            geoPoints[0].latitude,
                            geoPoints[0].longitude
                        )
                    )
                }

            }
        )
    }
}