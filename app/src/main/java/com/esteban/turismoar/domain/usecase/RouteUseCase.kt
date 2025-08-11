package com.esteban.turismoar.domain.usecase

import com.esteban.turismoar.domain.models.Route
import com.esteban.turismoar.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow

class GetAllRoutesUseCase(private val repository: RouteRepository) {
    operator fun invoke(): Flow<List<Route>> = repository.getAllRoutes()
}

