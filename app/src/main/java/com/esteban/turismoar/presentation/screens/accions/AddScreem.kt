package com.esteban.turismoar.presentation.screens.accions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.esteban.turismoar.presentation.components.inputs.InputTextField
import com.esteban.turismoar.presentation.components.inputs.Select
import com.esteban.turismoar.ui.theme.DarkGreen
import com.esteban.turismoar.ui.theme.White

@Composable
fun AddScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(16.dp)
    ) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clickable(onClick = {
                            navController.popBackStack()
                        })
                )
                Text(
                    "Add a place",
                    modifier = Modifier.align(Alignment.TopCenter),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Text(
                "Place details",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                "Provide us with the following information:",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            InputTextField(placeholder = "Name" )
            var options = listOf("Rute", "Mark", "Event", "Business")
            Select(options = options, placeholder = "Category")
            InputTextField(placeholder = "Description" )

        }
    }
}

