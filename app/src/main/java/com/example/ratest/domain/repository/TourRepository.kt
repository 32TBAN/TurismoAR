package com.example.ratest.domain.repository

interface TourRepository {
    suspend fun getVisitedPoints(): Set<String>
    suspend fun markPointAsVisited(name: String)
    suspend fun clearVisitedPoints()
}
