package com.example.ratest.presentation.viewmodels

import com.example.ratest.Utils.GeoPoint

sealed class TourUIState {
    object Loading : TourUIState()
    data class InProgress(val target: GeoPoint) : TourUIState()
    data class Arrived(val target: GeoPoint) : TourUIState()
    object Completed : TourUIState()
    data class Error(val message: String) : TourUIState()
}
