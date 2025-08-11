package com.esteban.turismoar.domain.repository

interface TourRepository {
    suspend fun getVisitedPoints(): Set<String>
    suspend fun markPointAsVisited(name: String)
    suspend fun clearVisitedPoints()
}
