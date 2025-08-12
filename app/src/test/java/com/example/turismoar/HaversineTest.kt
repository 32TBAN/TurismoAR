package com.esteban.turismoar

import org.junit.Assert.assertEquals
import org.junit.Test

class HaversineTest {

    @Test
    fun `haversineDistance between same points should be zero`() {
        val distance = haversineDistance(0.0, 0.0, 0.0, 0.0)
        assertEquals(0.0, distance, 0.0001)
    }

    @Test
    fun `haversineDistance should return correct value`() {
        val lat1 = 40.748817
        val lon1 = -73.985428
        val lat2 = 40.689247
        val lon2 = -74.044502

        val distance = haversineDistance(lat1, lon1, lat2, lon2)

        assertEquals(8300.0, distance, 200.0)
    }
}
