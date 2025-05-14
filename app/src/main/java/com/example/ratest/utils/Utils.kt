package com.example.ratest.utils

import com.example.ratest.R
import dev.romainguy.kotlin.math.Float3
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
        val earthRadius = 6378137.0

        val dLat = Math.toRadians(targetLat - currentLat)
        val dLon = Math.toRadians(targetLon - currentLon)
        val lat1Rad = Math.toRadians(currentLat)
        val lat2Rad = Math.toRadians(targetLat)

        // Fórmula de Haversine simplificada en plano local
        val deltaNorth = dLat * earthRadius
        val deltaEast = dLon * earthRadius * Math.cos((lat1Rad + lat2Rad) / 2.0)

        // ARCore coord system: +X = East, +Z = South, so -Z = North
        return Float3(
            deltaEast.toFloat(),
            0f,
            -deltaNorth.toFloat()
        )
    }


    fun rotateVectorByQuaternion(vector: Float3, quaternion: FloatArray): Float3 {
        // Convertimos el vector en un quaternion puro
        val x = vector.x
        val y = vector.y
        val z = vector.z

        val qx = quaternion[0]
        val qy = quaternion[1]
        val qz = quaternion[2]
        val qw = quaternion[3]

        // Rotación del vector usando la fórmula v' = q * v * q⁻¹
        val ix =  qw * x + qy * z - qz * y
        val iy =  qw * y + qz * x - qx * z
        val iz =  qw * z + qx * y - qy * x
        val iw = -qx * x - qy * y - qz * z

        val rx = ix * qw + iw * -qx + iy * -qz - iz * -qy
        val ry = iy * qw + iw * -qy + iz * -qx - ix * -qz
        val rz = iz * qw + iw * -qz + ix * -qy - iy * -qx

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