package com.example.ratest.presentation.Navigation

sealed class Screen(val route: String) {
    object Rutas : Screen("rutas")
    object Inicio : Screen("home")
    object Historia : Screen("history")
    object AR : Screen("ar/{model}") {
        fun createRoute(model: String) = "ar/$model"
    }
    object Alphabet : Screen("alphabet")
    object Quiz : Screen("quiz")
}
