package com.esteban.turismoar

import com.esteban.turismoar.domain.models.GeoPoint
import kotlin.math.*

fun isValidCoordinate(lat: Double, lon: Double): Boolean {
    return lat in -90.0..90.0 && lon in -180.0..180.0
}

fun haversineDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val R = 6371000.0 // Radio de la Tierra en metros
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c
}

fun filterNearbyPoints(
    currentLat: Double,
    currentLon: Double,
    geoPoints: List<GeoPoint>,
    maxDistanceMeters: Double = 100.0
): List<GeoPoint> {
    return geoPoints.filter {
        haversineDistance(currentLat, currentLon, it.latitude, it.longitude) <= maxDistanceMeters
    }
}
