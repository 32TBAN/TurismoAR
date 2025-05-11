package com.example.ratest.domain.usecase

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.data.local.UserPreferences
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.flow.first
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TourManager(private val context: Context) {

    private val geoPoints = mutableListOf<GeoPoint>()
    private var visitedPoints = mutableSetOf<String>()

    fun setGeoPoints(points: List<GeoPoint>) {
        geoPoints.clear()
        geoPoints.addAll(points)
    }

    suspend fun loadVisitedPoints() {
        val saved = UserPreferences.getVisitedPoints(context).first()
        visitedPoints.clear()
        visitedPoints.addAll(saved)
        Log.d("GeoAR", "Loaded visited points: $visitedPoints")
    }

    suspend fun markPointAsVisited(pointName: String) {
        UserPreferences.markPointAsVisited(context, pointName)
        visitedPoints.add(pointName)
    }

    fun getNextTarget(currentLat: Double, currentLon: Double): GeoPoint? {
        val notVisited = geoPoints.filter { it.name !in visitedPoints }
        return notVisited.minByOrNull { haversineDistance(currentLat, currentLon, it.latitude, it.longitude) }
    }

    fun getVisited(): Int {
        return geoPoints.filter { it.name in visitedPoints }.size
    }

    fun isAllVisited(): Boolean {
        return geoPoints.all { it.name in visitedPoints }
    }

    suspend fun clearVisitedPoints() {
        UserPreferences.clearAllVisitedPoints(context)
    }

    fun resetTour() {
        visitedPoints = mutableSetOf()
    }

    fun haversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371e3
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)

        val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
                cos(phi1) * cos(phi2) *
                sin(deltaLambda / 2) * sin(deltaLambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
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
}
