package com.example.ratest.Utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Engine
import com.google.android.filament.utils.Quaternion
import com.google.ar.core.Anchor
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import kotlin.collections.lastIndex
import kotlin.collections.plusAssign
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.withSign

object Utils {

    fun getModel(modelname: String): String {
        Log.e("GeoAR", modelname)
        return when (modelname) {
            "comidas" -> "models/frostmourne_with_animations.glb"
            "monumentos" -> "models/helm_of_domination.glb"
            "plazas" -> "models/anime_fox_girl.glb"
            "transportes" -> "models/warcraft_draenei_fanart.glb"
            "arrow" -> "models/arrow.glb"
            "pin" -> "models/navigation_pin.glb"
            else -> "models/warcraft_draenei_fanart.glb"
        }
    }

    fun createAnchorNode(
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstance: MutableList<ModelInstance>,
        anchor: Anchor,
        model: String
    ): AnchorNode {
        val anchorNode = AnchorNode(engine = engine, anchor = anchor)

        val modelNode = ModelNode(
            modelInstance = modelInstance.apply {
                this.clear()
                if (isEmpty()) {
                    try {
                        this += modelLoader.createInstancedModel(model, 10)
                    } catch (e: Exception) {
                        Log.e("ARScreen", "Error loading model: ${e.message}")
                    }
                }
            }.removeAt(modelInstance.lastIndex),
            scaleToUnits = 0.2f
        ).apply {
            isEditable = true
        }
        val boundingBox = CubeNode(
            engine = engine,
            size = modelNode.extents,
            center = modelNode.center,
            materialInstance = materialLoader.createColorInstance(Color.White)
        ).apply {
            isVisible = false
        }
        modelNode.addChildNode(boundingBox)
        anchorNode.addChildNode(modelNode)
        listOf(modelNode, anchorNode).forEach {
            it.onEditingChanged = { editingTransforms ->
                boundingBox.isVisible = editingTransforms.isNotEmpty()
            }
        }
        return anchorNode
    }

    fun quaternionToForward(q: FloatArray?): Float3 {
        // Verificamos que el array no sea nulo y tenga 4 elementos (para el cuaternión x, y, z, w)
        if (q == null || q.size != 4) {
            throw IllegalArgumentException("El cuaternión debe ser un FloatArray de tamaño 4")
        }

        // Extraer los componentes del cuaternión desde el FloatArray
        val qx = q[0]  // x
        val qy = q[1]  // y
        val qz = q[2]  // z
        val qw = q[3]  // w

        // Fórmula estándar para convertir el cuaternión en un vector de dirección (hacia adelante, eje Z)
        val forwardX = 2.0f * (qx * qz + qw * qy)
        val forwardY = 2.0f * (qy * qz - qw * qx)
        val forwardZ = 1.0f - 2.0f * (qx * qx + qy * qy)

        return Float3(forwardX, forwardY, forwardZ)
    }

    fun rotationBetweenVectors(from: Float3, to: Float3, out: FloatArray) {
        val dot = from.x * to.x + from.y * to.y + from.z * to.z
        val cross = Float3(
            from.y * to.z - from.z * to.y,
            from.z * to.x - from.x * to.z,
            from.x * to.y - from.y * to.x
        )
        val w =
            sqrt(((from.x * from.x + from.y * from.y + from.z * from.z) * (to.x * to.x + to.y * to.y + to.z * to.z)).toDouble()) + dot
        val x = cross.x.toFloat()
        val y = cross.y.toFloat()
        val z = cross.z.toFloat()
        val len = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        out[0] = x / len
        out[1] = y / len
        out[2] = z / len
        out[3] = w.toFloat()
    }


    fun quaternionToEulerAngles(q: Quaternion): Float3 {
        val sinr_cosp = 2.0f * (q.w * q.x + q.y * q.z)
        val cosr_cosp = 1.0f - 2.0f * (q.x * q.x + q.y * q.y)
        val roll = atan2(sinr_cosp.toDouble(), cosr_cosp.toDouble()).toFloat()  // Eje X

        val sinp = 2.0f * (q.w * q.y - q.z * q.x)
        val pitch = if (abs(sinp) >= 1) {
            (Math.PI.toFloat() / 2.0f).withSign(sinp)  // Eje Y
        } else {
            asin(sinp.toDouble())  // Eje Y
        }

        val siny_cosp = 2.0f * (q.w * q.z + q.x * q.y)
        val cosy_cosp = 1.0f - 2.0f * (q.y * q.y + q.z * q.z)
        val yaw = atan2(siny_cosp.toDouble(), cosy_cosp.toDouble()).toFloat()  // Eje Z

        return Float3(roll, pitch.toFloat(), yaw)  // Retorna los ángulos en Euler
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

    private fun Double.pow(exp: Double) = Math.pow(this, exp)

    fun directionToTarget(
        geoLat: Double,
        geoLng: Double,
        targetLat: Double,
        targetLng: Double
    ): Float3 {
        val dLat = Math.toRadians(targetLat - geoLat)
        val dLng = Math.toRadians(targetLng - geoLng)
        val y = Math.sin(dLng) * Math.cos(Math.toRadians(targetLat))
        val x = Math.cos(Math.toRadians(geoLat)) * Math.sin(Math.toRadians(targetLat)) -
                Math.sin(Math.toRadians(geoLat)) * Math.cos(Math.toRadians(targetLat)) * Math.cos(dLng)
        val bearing = Math.atan2(y, x) // en radianes
        val angle = Math.toDegrees(bearing)

        // Convertimos el ángulo en un vector hacia el norte relativo (Z+)
        val rad = Math.toRadians(angle)
        return Float3(Math.sin(rad).toFloat(), 0f, Math.cos(rad).toFloat())
    }


}