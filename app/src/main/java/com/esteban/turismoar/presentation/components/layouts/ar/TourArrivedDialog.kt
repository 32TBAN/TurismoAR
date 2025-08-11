package com.esteban.turismoar.presentation.components.layouts.ar

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.esteban.turismoar.presentation.components.layouts.CustomDialog

@Composable
fun TourArrivedDialog(
    targetName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomDialog(
        title = "¡Llegaste a $targetName!",
        onDismissRequest = onDismiss,
        content = { Text("¿Quieres marcar este lugar como visitado?", color = Color.DarkGray) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Sí", color = Color.White)
            }
        },
    ) {

    }
}
