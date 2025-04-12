package com.example.ratest.presentation.Navigation

import kotlinx.serialization.Serializable

@Serializable
object InicioScreen

@Serializable
data class RARScreen(val model: String)

@Serializable
object RoutesScreen

@Serializable
object HistoryScreen

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