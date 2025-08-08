package com.example.ratest.domain.usecase

import android.util.Log
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.domain.repository.TourRepository
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TourManager(
    private val repository: TourRepository
) {
    private val geoPoints = mutableListOf<GeoPoint>()
    private var visitedPoints = mutableSetOf<String>()

    fun setGeoPoints(points: List<GeoPoint>) {
        geoPoints.clear()
        geoPoints.addAll(points)
    }

    suspend fun loadVisitedPoints() {
        visitedPoints.clear()
        visitedPoints.addAll(repository.getVisitedPoints())
        Log.d("GeoAR", "Visited points: $visitedPoints")
    }

    suspend fun markPointAsVisited(pointName: String) {
        repository.markPointAsVisited(pointName)
        visitedPoints.add(pointName)
    }

    fun getNextTarget(currentLat: Double, currentLon: Double): GeoPoint? {
        val notVisited = geoPoints.filter { it.name !in visitedPoints }
        return notVisited.minByOrNull {
            haversineDistance(currentLat, currentLon, it.latitude, it.longitude)
        }
    }

    fun getVisited(): Int = geoPoints.count { it.name in visitedPoints }

    fun isAllVisited(): Boolean = geoPoints.all { it.name in visitedPoints }

    suspend fun clearVisitedPoints() {
        repository.clearVisitedPoints()
        visitedPoints.clear()
    }

    fun resetTour() {
        visitedPoints.clear()
    }

    fun haversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371e3
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)

        val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
                cos(phi1) * cos(phi2) *
                sin(deltaLambda / 2) * sin(deltaLambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }


}