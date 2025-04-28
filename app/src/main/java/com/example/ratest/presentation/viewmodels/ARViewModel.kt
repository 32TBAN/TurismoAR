package com.example.ratest.presentation.viewmodels


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.ratest.Utils.Utils
import com.google.android.filament.Engine
import com.google.ar.core.Earth
import com.google.ar.core.Frame
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.NotTrackingException
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import dev.romainguy.kotlin.math.Float3 as KotlinFloat3

class ARViewModel : ViewModel() {
    private val distanceTextMutable = MutableStateFlow("0.0 m")
    val distanceText: StateFlow<String> = distanceTextMutable

    private val isPinCreated = MutableStateFlow(false)

    val targetLatLng = Pair(-1.016238, -78.565148)
    val visibleRange = 7.0

    private val geoPoints = mutableStateListOf<Triple<Double, Double, String>>()
    private val visitedPoints = mutableStateListOf<String>()

    private val currentTargetMutable = MutableStateFlow<Triple<Double, Double, String>?>(null)
    val currentTarget: StateFlow<Triple<Double, Double, String>?> get() = currentTargetMutable

    fun setGeoPoints(points: List<Triple<Double, Double, String>>) {
        geoPoints.clear()
        geoPoints.addAll(points)
    }

    fun updateCurrentTarget(earth: Earth) {
        val geoPose = earth.cameraGeospatialPose
        val currentLat = geoPose.latitude
        val currentLon = geoPose.longitude

        val notVisitedPoints = geoPoints.filter { it.third !in visitedPoints }

        val closestPoint = notVisitedPoints.minByOrNull { point ->
            Utils.haversineDistance(currentLat, currentLon, point.first, point.second)
        }

        currentTargetMutable.value = closestPoint
    }

    fun updateSession(
        frame: Frame?, earth: Earth?,
        onAnchorCreated: (AnchorNode?) -> Unit,
        modelInstanceList: MutableList<ModelInstance>,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader
    ) {
        if (frame == null || earth == null) return
        try {
            if (earth.trackingState == TrackingState.TRACKING) {
                val geoPose = earth.cameraGeospatialPose

                val distance = Utils.haversineDistance(
                    geoPose.latitude,
                    geoPose.longitude,
                    targetLatLng.first,
                    targetLatLng.second
                )

                distanceTextMutable.value = "${"%.2f".format(distance)} m"

                if (distance <= visibleRange && !isPinCreated.value) {
                    val anchor = earth.createAnchor(
                        targetLatLng.first,
                        targetLatLng.second,
                        geoPose.altitude,
                        floatArrayOf(0f, 0f, 0f, 1f)
                    )
                    val anchorLatLng = earth.getGeospatialPose(anchor.pose).let {
                        val lat = it.latitude
                        val lng = it.longitude
                        val alt = it.altitude

                        Triple(lat, lng, alt)
                    }

                    val adjustedTargetPosition = KotlinFloat3(
                        anchorLatLng.first.toFloat(),
                        anchorLatLng.second.toFloat(),
                        geoPose.altitude.toFloat()
                    )
                    val pinNode = Utils.createAnchorNode(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstance = modelInstanceList,
                        anchor = anchor,
                        model = Utils.getModel("pin"),
                        scaleToUnits = 0.8f
                    )

                    pinNode.position = adjustedTargetPosition
                    onAnchorCreated(pinNode)
                    isPinCreated.value = true
                } else if (distance > visibleRange && isPinCreated.value) {
                    onAnchorCreated(null)
                    isPinCreated.value = false
                }
            }
        } catch (e: NotTrackingException) {
            Log.e("ARViewModel", "ARCore not tracking: ${e.message}")
        } catch (e: Exception) {
            Log.e("ARViewModel", "Unexpected error: ${e.message}")
        }
    }

    fun updateArrowNode(
        arrowNode: ModelNode,
        frame: Frame?,
        pinNode: AnchorNode?,
        targetLatLng: Pair<Double, Double>,
        earth: Earth?,
    ) {
        frame?.camera?.pose?.let { pose ->
            val forwardDirection = Utils.quaternionToForward(pose.rotationQuaternion)
            val distance = -0.3f
            arrowNode.position = KotlinFloat3(
                pose.tx() + forwardDirection.x * distance,
                pose.ty() - 0.2f,
                pose.tz() + forwardDirection.z * distance
            )

            val pinPosition = if (pinNode != null) {
                pinNode.worldPosition
            } else {
                val earth = earth ?: return
                val geoPose = earth.cameraGeospatialPose

                val offset = Utils.geoDistanceToLocal(
                    currentLat = geoPose.latitude,
                    currentLon = geoPose.longitude,
                    targetLat = targetLatLng.first,
                    targetLon = targetLatLng.second
                )

                KotlinFloat3(
                    pose.tx() + offset.x,
                    pose.ty(),
                    pose.tz() + offset.z
                )
            }

            arrowNode.lookAt(pinPosition, KotlinFloat3(0f, 1f, 0f))
        }
    }
}
