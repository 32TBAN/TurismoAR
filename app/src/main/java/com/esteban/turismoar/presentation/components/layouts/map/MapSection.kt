package com.esteban.turismoar.presentation.components.layouts.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color.BLUE
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.esteban.turismoar.presentation.mappers.UiRoute
import com.esteban.turismoar.ui.theme.DarkGreen
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import com.esteban.turismoar.domain.models.GeoPoint as GeoPointCustom


@Composable
fun MapSection(
    title: String? = null,
    geoPoints: List<GeoPointCustom> = emptyList(),
    zoomLevel: Double = 15.7,
    controls: Boolean = true,
    type: String = "",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onMarkerClick: ((GeoPointCustom) -> Unit)? = null
) {
    var selectedPoint by remember { mutableStateOf<GeoPointCustom?>(null) }
    Column{
        Box(modifier = Modifier.weight(3f)) {
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
                modifier = modifier.clip(RoundedCornerShape(8.dp)),
                factory = { context ->
                    createConfiguredMapView(
                        context = context,
                        geoPoints = geoPoints,
                        zoomLevel = zoomLevel,
                        type = type,
                        controls = controls,
                        onMarkerSelected = { selectedPoint = it }
                    )
                },
                update = { mapView ->
                    geoPoints.firstOrNull()?.let {
                        if (type == ""){
                            mapView.controller.setCenter(GeoPoint(it.latitude, it.longitude))
                            mapView.controller.setZoom(zoomLevel)
                        }
                    }
                }
            )
        }
        selectedPoint?.let { point ->
            Box(modifier = Modifier.weight(0.8f)) {
                Spacer(modifier = Modifier.height(8.dp))
                BottomMarkerInfo(
                    point = point,
                    onDetailsClick = {
                        onMarkerClick?.invoke(point)
                    },
                    onClose = { selectedPoint = null }
                )
            }
        }
    }
}


@SuppressLint("ClickableViewAccessibility")
fun createConfiguredMapView(
    context: Context,
    geoPoints: List<GeoPointCustom>,
    zoomLevel: Double,
    type: String,
    controls: Boolean,
    onMarkerSelected: ((GeoPointCustom) -> Unit)? = null
): MapView {
    Configuration.getInstance().userAgentValue = "com.esteban.ratest/1.0"

    return MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(controls)
        isClickable = controls
        controller.setZoom(zoomLevel)

        if (!controls) {
            // Bloquea toda interacción táctil
            setOnTouchListener { _, _ -> true }
        }

        val defaultPoint = geoPoints.firstOrNull()?.let {
            GeoPoint(it.latitude, it.longitude)
        } ?: GeoPoint(-1.04559, -78.59019)

        controller.setCenter(defaultPoint)

//        if (geoPoints.isNotEmpty()) {
            if (type == "ruta") {
                val polyline = Polyline().apply {
                    outlinePaint.strokeWidth = 10f
                    outlinePaint.color = BLUE
                    setPoints(geoPoints.map { GeoPoint(it.latitude, it.longitude) })
                }
                overlays.add(polyline)
            }

            geoPoints.forEach {
                if (it.name.isNotEmpty()) {
                    val marker = Marker(this).apply {
                        position = GeoPoint(it.latitude, it.longitude)
                        title = it.name
                        snippet = it.description
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

//                        val uiRoute = uiRoutes.find { route -> route.title == it.name }
//
//                        uiRoute?.let { route ->
//                            val iconDrawable = ContextCompat.getDrawable(context, route.icon)
//                            marker.icon = iconDrawable
//                        }

                        setOnMarkerClickListener { marker, _ ->
                            marker.showInfoWindow()
                            if (onMarkerSelected != null && type == "marcadorG"){
                                onMarkerSelected.invoke(it)
                            }
                            true
                        }
                    }

                    overlays.add(marker)
                }
            }
//        } else {
//            val marker = Marker(this).apply {
//                position = defaultPoint
//                title = "Salcedo"
//                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//            }
//            overlays.add(marker)
//        }

    }
}

