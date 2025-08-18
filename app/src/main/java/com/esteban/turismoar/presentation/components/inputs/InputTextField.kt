package com.esteban.turismoar.presentation.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esteban.turismoar.ui.theme.DarkGreen

@Composable
fun InputTextField(title: String? = null, placeholder: String, type: String? = null, onValueChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    val keyboardType = when (type) {
        "number" -> KeyboardType.Number
        "email" -> KeyboardType.Email
        "password" -> KeyboardType.Password
        "phone" -> KeyboardType.Phone
        "url" -> KeyboardType.Uri
        "decimal" -> KeyboardType.Decimal
        else -> KeyboardType.Text
    }



    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)){
        title?.let {
            Text(
                it,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp)
        }
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = when (type) {
                    "number" -> newText.filter { it.isDigit() }
                    "email" -> newText.filter { it.isLetterOrDigit() || it == '@' || it == '.' }
                    else -> newText
                }
                onValueChange(text)
            },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
            textStyle = TextStyle(color = DarkGreen),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            label = { Text(placeholder) },
            shape = RoundedCornerShape(8.dp)
        )
    }
}