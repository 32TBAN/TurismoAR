package com.example.ratest.Utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Engine
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Utils {

    fun getModel(modelname: String): String {
        Log.e("GeoAR", modelname)
        return when (modelname) {
            "iglesia" -> "models/Cartel_Iglesia.glb"
            else -> "models/navigation_pin.glb"
        }
    }

    fun createAnchorNode(
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstance: MutableList<ModelInstance>,
        anchor: Anchor,
        model: String,
        scaleToUnits: Float = 0.2f
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
            scaleToUnits = scaleToUnits
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
//        Log.d("GeoAR", "AnchorNode creado y agregado al árbol de nodos.")
        return anchorNode

    }

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
}