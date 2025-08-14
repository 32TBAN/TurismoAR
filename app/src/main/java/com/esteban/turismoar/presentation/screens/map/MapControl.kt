package com.esteban.turismoar.presentation.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.esteban.turismoar.domain.models.GeoPoint

@Composable
fun MapControl(
    isRoute: Boolean,
    onResult: (List<GeoPoint>) -> Unit,
){
    var selectPints by remember { mutableStateOf(listOf<GeoPoint>()) }


}