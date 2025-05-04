package com.example.ratest.presentation.mappers


import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.ratest.R
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.domain.models.Route

data class UiRoute(
    val id: Int,
    val title: String,
    val description: String,
    val imageRes: Int,
    val type: String,
    val geoPoints: List<GeoPoint>,
    val icon: ImageVector
)

@Composable
fun Route.toUiRoute(context: Context): UiRoute {
    val imageResId = context.resources.getIdentifier(
        this.imageRes, "drawable", context.packageName
    )
    val titleId = context.resources.getIdentifier(this.title, "string", context.packageName)
    val descriptionId = context.resources.getIdentifier(this.description, "string", context.packageName)

    val icon = when (this.id) {
        1 -> Icons.Default.Restaurant
        2 -> Icons.Default.AccountBalance
        3 -> Icons.Default.LocationCity
        4 -> Icons.Default.Diversity3
        5 -> Icons.Default.HistoryEdu
        6 -> Icons.Default.Gavel
        7 -> Icons.Filled.Church
        8 -> Icons.Default.Stadium
        9 -> Icons.Default.Storefront
        else -> Icons.Default.LocationOn
    }

    return UiRoute(
        id = id,
        title = stringResource(id = titleId),
        description = stringResource(id = descriptionId),
        imageRes = imageResId,
        type = type,
        geoPoints = geoPoints,
        icon = icon
    )
}

@Composable
fun List<Route>.toUiRoutes(context: Context): List<UiRoute> {
    return this.map { route ->
        route.toUiRoute(context)
    }
}

