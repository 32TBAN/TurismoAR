package com.esteban.turismoar.presentation.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.presentation.components.layouts.map.MapSection

@Composable
fun MapScreen(
    navController: NavController
){
    var selectPints by remember { mutableStateOf(listOf<GeoPoint>()) }
    Box(modifier = Modifier.fillMaxSize()){
        MapSection()
    }
}