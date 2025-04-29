package com.example.ratest.domain.usecase

import android.content.Context
import com.example.ratest.Utils.GeoPoint
import com.example.ratest.data.local.UserPreferences
import kotlinx.coroutines.flow.first

class TourManager(private val context: Context) {

    private val geoPoints = mutableListOf<GeoPoint>()
    private val visitedPoints = mutableSetOf<String>()

    fun setGeoPoints(points: List<GeoPoint>) {
        geoPoints.clear()
        geoPoints.addAll(points)
    }

    suspend fun loadVisitedPoints() {
        val saved = UserPreferences.getVisitedPoints(context).first()
        visitedPoints.clear()
        visitedPoints.addAll(saved)
    }

    suspend fun markPointAsVisited(pointName: String) {
        UserPreferences.markPointAsVisited(context, pointName)
        visitedPoints.add(pointName)
    }

    fun getNextTarget(currentLat: Double, currentLon: Double): GeoPoint? {
        val notVisited = geoPoints.filter { it.name !in visitedPoints }
        return notVisited.minByOrNull { haversineDistance(currentLat, currentLon, it.latitude, it.longitude) }
    }

    fun isAllVisited(): Boolean {
        return geoPoints.all { it.name in visitedPoints }
    }

    fun haversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371e3
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)

        val a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return R * c
    }
}
