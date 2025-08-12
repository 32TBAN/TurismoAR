package com.esteban.turismoar.presentation.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.domain.models.Route
import com.esteban.turismoar.domain.usecase.GetAllRoutesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RouteViewModel(
    private val getAllRoutesUseCase: GetAllRoutesUseCase
) : ViewModel() {

    private val _allRoutes = MutableStateFlow<List<Route>>(emptyList())
    val allRoutes: StateFlow<List<Route>> = _allRoutes.asStateFlow()

    val tourRoutes = _allRoutes.map { it.filter { it.type == "rute" } }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5_000), emptyList())

    val markersRoutes = _allRoutes.map { it.filter { it.type == "marcador" } }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5_000), emptyList())

    private val _selectedRoute = MutableStateFlow<Route?>(null)
    val selectedRoute: StateFlow<Route?> = _selectedRoute.asStateFlow()

    init {
        viewModelScope.launch {
            getAllRoutesUseCase().collect {
                _allRoutes.value = it
            }
        }
    }

    fun selectRouteById(routeId: Int) {
        _selectedRoute.value = _allRoutes.value.find { it.id == routeId }
    }

    fun getGeoPointsForRoute(routeId: Int): List<GeoPoint> {
        return _allRoutes.value.find { it.id == routeId }?.geoPoints ?: emptyList()
    }
}