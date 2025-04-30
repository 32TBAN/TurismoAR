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

    private val isPinCreated = MutableStateFlow(false)
    private val uiStateMutable = MutableStateFlow<TourUIState>(TourUIState.Loading)
    val uiState: StateFlow<TourUIState> = uiStateMutable

    val visibleRange = 4.9

    fun initialize(context: Context, geoPoints: List<GeoPoint>) {
        tourManager = TourManager(context)
        Log.d("GeoAR", "GeoAR: $geoPoints")
        tourManager.setGeoPoints(geoPoints)

        viewModelScope.launch {
            try {
                tourManager.loadVisitedPoints()
                uiStateMutable.value = TourUIState.InProgress(
                    target = tourManager.getNextTarget(0.0, 0.0) ?: GeoPoint(
                        0.0,
                        0.0,
                        "Sin destino"
                    )
                )
                Log.d("GeoAR", "Initialized with target: ${uiStateMutable.value}")
            } catch (e: Exception) {
                uiStateMutable.value = TourUIState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun updateTarget(currentLat: Double?, currentLon: Double?) {
        if (currentLat == null || currentLon == null) return
        val target = tourManager.getNextTarget(currentLat, currentLon)
        if (tourManager.isAllVisited()) {
            uiStateMutable.value = TourUIState.Completed
        } else if (target != null) {
//            Log.d("GeoAR", "Updating target to: $target")
            uiStateMutable.value = TourUIState.InProgress(target)
        }
    }

    fun markCurrentTargetVisited() {
        viewModelScope.launch {
            when (val state = uiStateMutable.value) {

                is TourUIState.Arrived -> {
                    tourManager.markPointAsVisited(state.target.name)
                    val nextTarget =
                        tourManager.getNextTarget(state.target.latitude, state.target.longitude)
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

    fun restartTour() {
        viewModelScope.launch {
            try {
                tourManager.clearVisitedPoints()

                tourManager.resetTour()

                val firstTarget = tourManager.getNextTarget(0.0, 0.0)
                if (firstTarget != null) {
                    uiStateMutable.value = TourUIState.InProgress(firstTarget)
                } else {
                    uiStateMutable.value = TourUIState.Error("No hay destinos disponibles.")
                }

                isPinCreated.value = false
            } catch (e: Exception) {
                uiStateMutable.value = TourUIState.Error(e.localizedMessage ?: "Error reiniciando tour")
            }
        }
    }

    fun getZisedVisitedPoints(): Int {
        return tourManager.visitedPoints.size
    }

    fun updateSession(
        frame: Frame?, earth: Earth?,
        onAnchorCreated: (AnchorNode?) -> Unit,
        modelInstanceList: MutableList<ModelInstance>,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        nameModel: String = "pin"
    ) {
        if (frame == null || earth == null) return
        val currentTarget = (uiStateMutable.value as? TourUIState.InProgress)?.target
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

                if (nameModel == "pin" && distance <= 2) {
                    uiStateMutable.value = TourUIState.Arrived(currentTarget)
                }

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
                        model = Utils.getModel(nameModel),
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
            val hudDistance = -0.2f
            val hudYOffset = 0.2f
            arrowNode.position = KotlinFloat3(
                pose.tx() + forwardDirection.x * hudDistance,
                pose.ty() - hudYOffset,
                pose.tz() + forwardDirection.z * hudDistance
            )

            val pinPosition = if (pinNode != null) {
                pinNode.worldPosition
            } else {
                val currentTarget = (uiStateMutable.value as? TourUIState.InProgress)?.target
                if (currentTarget == null) return

                val earth = earth ?: return
                val geoPose = earth.cameraGeospatialPose

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
