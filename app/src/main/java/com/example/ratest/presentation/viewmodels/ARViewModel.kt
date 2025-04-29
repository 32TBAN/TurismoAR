package com.example.ratest.presentation.viewmodels


import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ratest.Utils.GeoPoint
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
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.ratest.domain.usecase.TourManager
import kotlinx.coroutines.launch

class ARViewModel : ViewModel() {
    private lateinit var tourManager: TourManager

    private val distanceTextMutable = MutableStateFlow("0.0 m")
    val distanceText: StateFlow<String> = distanceTextMutable

    private val currentTargetMutable = MutableStateFlow<GeoPoint?>(null)
    val currentTarget: StateFlow<GeoPoint?> get() = currentTargetMutable

    private val allVisitedMutable = mutableStateOf(false)
    val allVisited: State<Boolean> = allVisitedMutable

    private val isPinCreated = MutableStateFlow(false)
    val visibleRange = 5.0

    private val uiStateMutable = MutableStateFlow<TourUIState>(TourUIState.Loading)
    val uiState: StateFlow<TourUIState> = uiStateMutable

    fun initialize(context: Context, geoPoints: List<GeoPoint>) {
        tourManager = TourManager(context)
        tourManager.setGeoPoints(geoPoints)

        viewModelScope.launch {
            try {
                tourManager.loadVisitedPoints()
                uiStateMutable.value = TourUIState.InProgress(
                    target = tourManager.getNextTarget(0.0, 0.0) ?: GeoPoint(0.0, 0.0, "Sin destino")
                )
            } catch (e: Exception) {
                uiStateMutable.value = TourUIState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun updateTarget(currentLat: Double, currentLon: Double) {
        val target = tourManager.getNextTarget(currentLat, currentLon)
        if (tourManager.isAllVisited()) {
            uiStateMutable.value = TourUIState.Completed
        } else if (target != null) {
            uiStateMutable.value = TourUIState.InProgress(target)
        }
    }

    fun markCurrentTargetVisited() {
        viewModelScope.launch {
            when (val state = uiStateMutable.value) {
                is TourUIState.InProgress -> {
                    tourManager.markPointAsVisited(state.target.name)
                    val nextTarget = tourManager.getNextTarget(state.target.latitude, state.target.longitude)
                    if (tourManager.isAllVisited()) {
                        uiStateMutable.value = TourUIState.Completed
                    } else if (nextTarget != null) {
                        uiStateMutable.value = TourUIState.InProgress(nextTarget)
                    }
                }
                else -> {}
            }
        }
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
        val currentTarget = currentTargetMutable.value
        if (currentTarget == null) return
        try {
            if (earth.trackingState == TrackingState.TRACKING) {
                val geoPose = earth.cameraGeospatialPose

                val distance = Utils.haversineDistance(
                    geoPose.latitude,
                    geoPose.longitude,
                    currentTarget.latitude,
                    currentTarget.longitude
                )

                distanceTextMutable.value = "${"%.2f".format(distance)} m"

                if (distance <= visibleRange && !isPinCreated.value) {
                    val anchor = earth.createAnchor(
                        currentTarget.latitude,
                        currentTarget.longitude,
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
                val currentTarget = currentTargetMutable.value
                if (currentTarget == null) return

                val offset = Utils.geoDistanceToLocal(
                    currentLat = geoPose.latitude,
                    currentLon = geoPose.longitude,
                    targetLat = currentTarget.latitude,
                    targetLon = currentTarget.longitude
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
