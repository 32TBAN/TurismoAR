package com.esteban.turismoar.domain.repository

import com.esteban.turismoar.domain.models.Route
import com.esteban.turismoar.domain.models.GeoPoint
import kotlinx.coroutines.flow.Flow

interface RouteRepository {
    fun getAllRoutes(): Flow<List<Route>>
    fun getGeoPointsByRute(rute: Int): Flow<List<GeoPoint>>
}
