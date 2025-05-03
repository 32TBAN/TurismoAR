package com.example.ratest.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratest.data.local.RouteDataSource
import com.example.ratest.data.repository.RouteRepositoryImpl
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.domain.repository.RouteRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RouteViewModel : ViewModel() {
    private lateinit var repository: RouteRepository

    private val _geoPoints = MutableStateFlow<List<GeoPoint>>(emptyList())
    val geoPoints: StateFlow<List<GeoPoint>> = _geoPoints

    fun initialize(context: Context) {
        val dataSource = RouteDataSource(context)
        repository = RouteRepositoryImpl(dataSource)
    }

    fun loadGeoPoints(routeId: Int) {
        viewModelScope.launch {
            val points = repository.getGeoPointsByRute(routeId)
            _geoPoints.value = points as List<GeoPoint>
        }
    }
}


