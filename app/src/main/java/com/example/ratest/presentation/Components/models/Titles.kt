package com.example.ratest.presentation.Components.models

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier.padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}
