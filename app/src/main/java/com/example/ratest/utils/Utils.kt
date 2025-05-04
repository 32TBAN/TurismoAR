package com.example.ratest.utils

import com.example.ratest.R
import dev.romainguy.kotlin.math.Float3

object Utils {

    fun quaternionToForward(q: FloatArray?): Float3 {
        if (q == null || q.size != 4) {
            throw IllegalArgumentException("El cuaternión debe ser un FloatArray de tamaño 4")
        }

        val qx = q[0]
        val qy = q[1]
        val qz = q[2]
        val qw = q[3]

        // Fórmula estándar para convertir el cuaternión en un vector de dirección (hacia adelante, eje Z)
        val forwardX = 2.0f * (qx * qz + qw * qy)
        val forwardY = 2.0f * (qy * qz - qw * qx)
        val forwardZ = 1.0f - 2.0f * (qx * qx + qy * qy)

        return Float3(forwardX, forwardY, forwardZ)
    }

    private const val METERS_PER_DEGREE_LAT = 111320.0
    private const val METERS_PER_DEGREE_LON_AT_EQUATOR = 111320.0
    fun geoDistanceToLocal(
        currentLat: Double,
        currentLon: Double,
        targetLat: Double,
        targetLon: Double
    ): Float3 {
        val deltaLat = targetLat - currentLat
        val deltaLon = targetLon - currentLon

        val metersNorth = deltaLat * METERS_PER_DEGREE_LAT
        val metersEast = deltaLon * METERS_PER_DEGREE_LON_AT_EQUATOR

        return Float3(metersEast.toFloat(), 0f, -metersNorth.toFloat())
    }

    fun getList(): List<Int> {
        return listOf(
            R.drawable.comidas,
            R.drawable.palacio_municipal,
            R.drawable.parque_ai,
            R.drawable.iglesia_ai,
            R.drawable.plazas,
            R.drawable.mercado_ai,
            R.drawable.coliseo_ai,
            R.drawable.monumento_fray_ia,
            R.drawable.monumento_madre_ai
        )

    }
}