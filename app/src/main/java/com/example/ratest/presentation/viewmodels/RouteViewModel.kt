package com.example.ratest.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratest.data.local.RouteDataSource
import com.example.ratest.data.repository.RouteRepositoryImpl
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.domain.models.Route
import com.example.ratest.domain.repository.RouteRepository
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted

class RouteViewModel : ViewModel() {
    private lateinit var repository: RouteRepository

    private val _allRoutes = MutableStateFlow<List<Route>>(emptyList())
    val allRoutes: StateFlow<List<Route>> = _allRoutes.asStateFlow()

    val tourRoutes: StateFlow<List<Route>> = _allRoutes
        .map { it.filter { route -> route.type == "ruta" } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val brandRoutes: StateFlow<List<Route>> = _allRoutes
        .map { it.filter { route -> route.type == "marca" } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _selectedRoute = MutableStateFlow<Route?>(null)
    val selectedRoute: StateFlow<Route?> = _selectedRoute.asStateFlow()

    fun initialize(context: Context) {
        val dataSource = RouteDataSource(context)
        repository = RouteRepositoryImpl(dataSource)

        viewModelScope.launch {
            repository.getAllRoutes().collect {
                _allRoutes.value = it
            }
        }
    }

    fun selectRouteById(routeId: Int) {
        val route = _allRoutes.value.find { it.id == routeId }
        _selectedRoute.value = route
    }

    fun getGeoPointsForRoute(routeId: Int): List<GeoPoint> {
        return _allRoutes.value.find { it.id == routeId }?.geoPoints ?: emptyList()
    }
}


