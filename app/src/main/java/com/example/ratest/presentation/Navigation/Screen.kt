package com.example.ratest.presentation.Navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Rutas : Screen("rutas")
    object Historia : Screen("historia")
}