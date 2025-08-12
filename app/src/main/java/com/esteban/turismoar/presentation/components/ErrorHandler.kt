package com.esteban.turismoar.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.esteban.turismoar.presentation.components.layouts.CustomDialog

import com.esteban.turismoar.utils.ErrorState

@Composable
fun ErrorHandler(
    errorState: ErrorState,
    navController: NavController? = null,
    fallbackAction: (() -> Unit)? = null
) {
    val error = errorState.error.value

    if (error != null) {
        CustomDialog(
            title = "Error",
            onDismissRequest = {
                errorState.clear()
                fallbackAction?.invoke() ?: navController?.popBackStack()
            },
            confirmButton = {
                Button(onClick = {
                    errorState.clear()
                    fallbackAction?.invoke() ?: navController?.popBackStack()
                }) {
                    Text("Aceptar", color = Color.White)
                }
            },
            content = {
                Text(
                    "Ocurrió un error inesperado. Inténtalo más tarde o contacta a soporte.\n\n${error.message}"
                )
            }
        )
    }
}
