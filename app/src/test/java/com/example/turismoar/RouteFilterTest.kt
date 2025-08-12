package com.esteban.turismoar

import com.esteban.turismoar.domain.models.GeoPoint
import org.junit.Assert.*
import org.junit.Test

class RouteFilterTest {

    private val points = listOf(
        GeoPoint(40.0, -74.0, "Cerca", "", ""),
        GeoPoint(10.0, -50.0, "Lejos", "", "")
    )

    @Test
    fun `should return only nearby points`() {
        val result = filterNearbyPoints(
            currentLat = 40.0001,
            currentLon = -74.0001,
            geoPoints = points,
            maxDistanceMeters = 500.0
        )

        assertEquals(1, result.size)
        assertEquals("Cerca", result[0].name)
    }
}
