package com.esteban.turismoar.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
data class RARScreen(val routeId: Int, val type: String)

@Serializable
object RoutesScreen

@Serializable
object HistoryScreen

@Serializable
data class DetailScreen(val routeId: Int)

val screens = listOf(
    HomeScreen,
    RoutesScreen,
    HistoryScreen
)

val lables = listOf(
    "Inicio",
    "Rutas",
    "Historia"
)

val titlesTopBar = listOf(
    HomeScreen to "Inicio",
    RoutesScreen to "Explora las rutas turísticas",
    HistoryScreen to "Historia local"
)