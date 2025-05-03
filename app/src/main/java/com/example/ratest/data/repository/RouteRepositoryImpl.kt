package com.example.ratest.data.repository

import com.example.ratest.data.local.RouteDataSource
import com.example.ratest.domain.models.Route
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RouteRepositoryImpl(
    private val localDataSource: RouteDataSource
) : RouteRepository {
    override fun getAllRoutes(): Flow<List<Route>> = flow {
        emit(localDataSource.getRoutes())
    }

    override fun getGeoPointsByRute(rute: Int): Flow<List<GeoPoint>> = flow {
        val route = localDataSource.getRoutes().firstOrNull { it.id == rute }
        emit(route?.geoPoints ?: emptyList())
    }
}
