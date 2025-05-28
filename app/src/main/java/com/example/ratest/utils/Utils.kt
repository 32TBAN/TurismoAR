package com.example.ratest.utils

import com.example.ratest.R
import dev.romainguy.kotlin.math.Float3
import kotlin.math.cos

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

//    private const val METERS_PER_DEGREE_LAT = 111320.0
//    private const val METERS_PER_DEGREE_LON_AT_EQUATOR = 111320.0

    fun geoDistanceToLocal(
        currentLat: Double,
        currentLon: Double,
        targetLat: Double,
        targetLon: Double
    ): Float3 {
        // Radio medio de la Tierra en metros
        val earthRadius = 6371e3

        val dLat = Math.toRadians(targetLat - currentLat)
        val dLon = Math.toRadians(targetLon - currentLon)
        val avgLat = Math.toRadians((currentLat + targetLat) / 2)

        val deltaNorth = dLat * earthRadius
        val deltaEast = dLon * earthRadius * cos(avgLat)

        return Float3(
            deltaEast.toFloat(),
            0f,
            -deltaNorth.toFloat()
        )
    }


    fun rotateVectorByQuaternion(v: Float3, q: FloatArray): Float3 {
        val x = q[0]; val y = q[1]; val z = q[2]; val w = q[3]
        val vx = v.x; val vy = v.y; val vz = v.z

        val rx = (1 - 2 * y * y - 2 * z * z) * vx + (2 * x * y - 2 * w * z) * vy + (2 * x * z + 2 * w * y) * vz
        val ry = (2 * x * y + 2 * w * z) * vx + (1 - 2 * x * x - 2 * z * z) * vy + (2 * y * z - 2 * w * x) * vz
        val rz = (2 * x * z - 2 * w * y) * vx + (2 * y * z + 2 * w * x) * vy + (1 - 2 * x * x - 2 * y * y) * vz

        return Float3(rx, ry, rz)
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