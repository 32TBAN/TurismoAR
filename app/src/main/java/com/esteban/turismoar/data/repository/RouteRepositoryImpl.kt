package com.esteban.turismoar.data.repository

import com.esteban.turismoar.data.remote.RouteDataSourceFirestore
import com.esteban.turismoar.domain.models.Route
import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RouteRepositoryImpl(
    private val localDataSource: RouteDataSourceFirestore
) : RouteRepository {
    override fun getAllRoutes(): Flow<List<Route>> = flow {
        emit(localDataSource.getRoutes())
    }

    override fun getGeoPointsByRute(rute: Int): Flow<List<GeoPoint>> = flow {
        val route = localDataSource.getRoutes().firstOrNull { it.id == rute }
        emit(route?.geoPoints ?: emptyList())
    }
}
