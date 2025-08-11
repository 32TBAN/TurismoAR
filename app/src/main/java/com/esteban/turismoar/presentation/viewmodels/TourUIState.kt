package com.esteban.turismoar.presentation.viewmodels

import com.esteban.turismoar.domain.models.GeoPoint

sealed class TourUIState {
    object Loading : TourUIState()
    data class InProgress(val target: GeoPoint) : TourUIState()
    data class Arrived(val target: GeoPoint) : TourUIState()
    object Completed : TourUIState()
    data class Error(val message: String) : TourUIState()

}
