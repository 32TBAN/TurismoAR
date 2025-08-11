package com.esteban.turismoar

import org.junit.Assert.*
import org.junit.Test

class GeoValidationTest {

    @Test
    fun `valid coordinate should return true`() {
        assertTrue(isValidCoordinate(40.0, -74.0))
    }

    @Test
    fun `invalid latitude should return false`() {
        assertFalse(isValidCoordinate(-100.0, -74.0))
    }

    @Test
    fun `invalid longitude should return false`() {
        assertFalse(isValidCoordinate(40.0, -200.0))
    }
}
