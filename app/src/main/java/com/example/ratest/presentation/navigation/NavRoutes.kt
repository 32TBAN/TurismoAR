package com.example.ratest.presentation.Navigation

import com.example.ratest.Utils.GeoPoint
import kotlinx.serialization.Serializable

@Serializable
object InicioScreen

@Serializable
data class RARScreen(val routeId: Int, val type: String)

@Serializable
object RoutesScreen

@Serializable
object HistoryScreen

@Serializable
data class DetalleScreen(val routeId: Int)

val screens = listOf(
    InicioScreen,
    RoutesScreen,
    HistoryScreen
)

val lables = listOf(
    "Inicio",
    "Rutas",
    "Historia"
)

val titlesTopBar = listOf(
    InicioScreen to "Inicio",
    RoutesScreen to "Explora las rutas turísticas",
    HistoryScreen to "Historia local"
)