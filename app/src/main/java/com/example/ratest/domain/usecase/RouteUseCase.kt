package com.example.ratest.domain.usecase

import com.example.ratest.domain.models.Route
import com.example.ratest.domain.repository.RouteRepository
import com.example.ratest.domain.models.GeoPoint
import kotlinx.coroutines.flow.Flow

class GetAllRoutesUseCase(private val repository: RouteRepository) {
    operator fun invoke(): Flow<List<Route>> = repository.getAllRoutes()
}

class GetGeoPointsByRouteUseCase(private val repository: RouteRepository) {
    operator fun invoke(rute: Int): Flow<List<GeoPoint>> = repository.getGeoPointsByRute(rute)
}

