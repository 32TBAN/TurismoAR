package com.example.ratest.domain.repository

import com.example.ratest.domain.models.Route
import com.example.ratest.domain.models.GeoPoint
import kotlinx.coroutines.flow.Flow

interface RouteRepository {
    fun getAllRoutes(): Flow<List<Route>>
    fun getGeoPointsByRute(rute: Int): Flow<List<GeoPoint>>
}
