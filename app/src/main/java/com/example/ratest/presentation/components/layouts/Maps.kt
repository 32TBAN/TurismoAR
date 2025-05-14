package com.example.ratest.presentation.components.layouts

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
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
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.Green
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import com.example.ratest.domain.models.GeoPoint as GeoPointCustom


@Composable
fun MapSection(
    title: String? = null,
    geoPoints: List<GeoPointCustom> = emptyList(),
    zoomLevel: Double = 15.7,
    controls: Boolean = true,
    type: String = "ruta",
    modifier: Modifier
) {

    Column {
        title?.let {
            Text(
                text = it,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        AndroidView(
            modifier = modifier.clip(RoundedCornerShape(16.dp)),
            factory = { context ->
                createConfiguredMapView(
                    context = context,
                    geoPoints = geoPoints,
                    zoomLevel = zoomLevel,
                    type = type,
                    controls = controls
                )
            },
            update = { mapView ->
                geoPoints.firstOrNull()?.let {
                    mapView.controller.setCenter(GeoPoint(it.latitude, it.longitude))
                    mapView.controller.setZoom(zoomLevel)
                }
            }
        )
    }
}

fun createConfiguredMapView(
    context: Context,
    geoPoints: List<GeoPointCustom>,
    zoomLevel: Double,
    type: String,
    controls: Boolean
): MapView {
    Configuration.getInstance().userAgentValue = "com.example.ratest/1.0"

    return MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(controls)

        controller.setZoom(zoomLevel)

        val defaultPoint = geoPoints.firstOrNull()?.let {
            GeoPoint(it.latitude, it.longitude)
        } ?: GeoPoint(-1.04559, -78.59019)

        controller.setCenter(defaultPoint)

        if (geoPoints.isNotEmpty()) {
            val polyline = Polyline().apply {
                outlinePaint.strokeWidth = 10f
                outlinePaint.color = android.graphics.Color.BLUE
                setPoints(geoPoints.map { GeoPoint(it.latitude, it.longitude) })
            }
            overlays.add(polyline)

            if (type == "ruta" || type == "marcador") {
                geoPoints.forEach {
                    if (it.name.isNotEmpty()) {
                        val marker = Marker(this).apply {
                            position = GeoPoint(it.latitude, it.longitude)
                            title = it.name
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }
                        overlays.add(marker)
                    }
                }
            }
        } else {
            val marker = Marker(this).apply {
                position = defaultPoint
                title = "Salcedo"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            overlays.add(marker)
        }
    }
}
