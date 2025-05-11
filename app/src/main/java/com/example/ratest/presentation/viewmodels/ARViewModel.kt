package com.example.ratest.presentation.viewmodels


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.utils.Utils
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
import androidx.lifecycle.viewModelScope
import com.example.ratest.domain.usecase.TourManager
import kotlinx.coroutines.launch
import io.github.sceneview.ar.ARSceneView
import androidx.core.graphics.createBitmap
import com.google.android.gms.location.LocationServices
import com.google.ar.core.Anchor
import io.github.sceneview.node.Node
import java.io.File
import kotlin.math.abs
import kotlinx.coroutines.tasks.await

@SuppressLint("StaticFieldLeak")
class ARViewModel : ViewModel() {
    private lateinit var tourManager: TourManager

    private val distanceTextMutable = MutableStateFlow("0.0 m")
    val distanceText: StateFlow<String> = distanceTextMutable

    private val isPinCreated = MutableStateFlow(false)
    private val uiStateMutable = MutableStateFlow<TourUIState>(TourUIState.Loading)
    val uiState: StateFlow<TourUIState> = uiStateMutable

    val visibleRange = 5.99

    val arNodes = mutableStateListOf<Node>()
    var arSceneView: ARSceneView? = null
    val modelInstanceList = mutableListOf<ModelInstance>()
    var imageUriState = mutableStateOf<Uri?>(null)

    private var stableTrackingFrames = 0
    private val requiredStableFrames = 10
    var  currentPosition = mutableStateOf<GeoPoint?>(GeoPoint(0.0, 0.0, "Posición actual", ""))
    var isTrackingStable = mutableStateOf(false)
    val selectedModelPath = mutableStateOf<String?>(null)
    val scaleModel = mutableStateOf<Float>(0.6f)

    fun createModelNode(anchor: Anchor) {
        val nodeModel = tourManager.createAnchorNode(
            engine = arSceneView!!.engine,
            modelLoader = arSceneView!!.modelLoader,
            materialLoader = arSceneView!!.materialLoader,
            modelInstance = modelInstanceList,
            anchor = anchor,
            model = selectedModelPath.value!!,
            scaleToUnits = scaleModel.value
        )
        arSceneView?.addChildNode(nodeModel)
    }

    fun initialize(context: Context, geoPoints: List<GeoPoint>) {
        tourManager = TourManager(context)
//        Log.d("GeoAR", "GeoAR: $geoPoints")
        tourManager.setGeoPoints(geoPoints)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        viewModelScope.launch {
            try {
                val location = try {
                    fusedLocationClient.lastLocation.await()
                } catch (e: Exception) {
                    null
                }

                tourManager.loadVisitedPoints()
                //todo: poner la posicion inicial del usuario
                uiStateMutable.value = TourUIState.InProgress(
                    target = tourManager.getNextTarget(0.0, 0.0) ?: GeoPoint(
                        0.0,
                        0.0,
                        "Sin destino",
                        ""
                    )
                )
//                Log.d("GeoAR", "Initialized with target: ${uiStateMutable.value}")
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

                arSceneView?.removeChildNodes(arNodes)
                arNodes.clear()
                isPinCreated.value = false
            } catch (e: Exception) {
                uiStateMutable.value =
                    TourUIState.Error(e.localizedMessage ?: "Error reiniciando tour")
            }
        }
    }

    fun getZisedVisitedPoints(): Int {
        return tourManager.getVisited()
    }

    fun updateSession(
        frame: Frame?,
        earth: Earth?,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        type: String = "ruta"
    ) {
        if (frame == null || earth == null) return
        val currentTarget = (uiStateMutable.value as? TourUIState.InProgress)?.target
        if (currentTarget == null) return
        try {
            if (earth.trackingState != TrackingState.TRACKING) {
                uiStateMutable.value = TourUIState.Loading
                isTrackingStable.value = false
                stableTrackingFrames = 0
                return
            }
            val geoPose = earth.cameraGeospatialPose

            val deltaLat = abs((currentPosition.value?.latitude ?: geoPose.latitude) - geoPose.latitude)
            val deltaLon = abs((currentPosition.value?.longitude ?: geoPose.longitude) - geoPose.longitude)

            if (deltaLat < 0.00001 && deltaLon < 0.00001) {
                stableTrackingFrames++
            } else {
                stableTrackingFrames = 0
            }

            currentPosition.value?.latitude  = geoPose.latitude
            currentPosition.value?.longitude  = geoPose.longitude

            if (stableTrackingFrames >= requiredStableFrames) {
                isTrackingStable.value = true
            } else {
                uiStateMutable.value = TourUIState.Loading
                return
            }

            if (earth.trackingState == TrackingState.TRACKING) {

                val distance = tourManager.haversineDistance(
                    geoPose.latitude,
                    geoPose.longitude,
                    currentTarget.latitude,
                    currentTarget.longitude
                )

                distanceTextMutable.value = "${"%.2f".format(distance)} m"

//                Log.d("GeoAR", type)
                if (type == "ruta" && distance <= 2) {
                    arSceneView?.removeChildNodes(arNodes)
                    uiStateMutable.value = TourUIState.Arrived(currentTarget)
                }

                if (distance <= visibleRange && !isPinCreated.value) {
//                    Log.d("GeoAR", "Creating anchor for: $currentTarget")
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

//                    Log.d("GeoAR", "Anchor created at: $anchorLatLng")
                    val adjustedTargetPosition = KotlinFloat3(
                        anchorLatLng.first.toFloat(),
                        geoPose.longitude.toFloat(),
                        geoPose.altitude.toFloat()
                    )
                    val pinNode = tourManager.createAnchorNode(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstance = modelInstanceList,
                        anchor = anchor,
                        model = currentTarget.model,
                        scaleToUnits = 2.6f
                    )

                    pinNode.position = adjustedTargetPosition
                    pinNode.position = KotlinFloat3(
                        pinNode.position.x,
                        0f,
                        pinNode.position.z
                    )

                    arSceneView?.addChildNode(pinNode)
                    arNodes.add(pinNode)
                    isPinCreated.value = true
                } else if (distance > visibleRange && isPinCreated.value) {
                    arNodes.firstOrNull()?.let {
                        arSceneView?.removeChildNode(it)
                        arNodes.remove(it)
                        isPinCreated.value = false
                    }
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
            val hudYOffset = 0.1f

            arrowNode.isVisible = selectedModelPath.value == null

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
//                val rotatedOffset = Utils.rotateVectorByQuaternion(offset, geoPose.eastUpSouthQuaternion)

                KotlinFloat3(
                    pose.tx() + offset.x,
                    pose.ty(),
                    pose.tz() + offset.z
                )
            }

            arrowNode.lookAt(pinPosition, KotlinFloat3(0f, 1f, 0f))
        }
    }

    fun captureARView(arView: ARSceneView, onCaptured: (Bitmap?) -> Unit) {
        val bitmap = createBitmap(arView.width, arView.height)

        PixelCopy.request(arView, bitmap, { result ->
            if (result == PixelCopy.SUCCESS) {
                onCaptured(bitmap)
            } else {
                Log.e("GeoAR", "PixelCopy failed with result code: $result")
                onCaptured(null)
            }
        }, Handler(Looper.getMainLooper()))
    }

    fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "screenshot_${System.currentTimeMillis()}.png")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
//        Log.e("GeoAR", "Se tom")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    fun onTakeScreenshot(context: Context) {
        arSceneView?.let {
            captureARView(it) { bitmap ->
                if (bitmap != null) {
                    val uri = saveBitmapToCache(context, bitmap)
                    imageUriState.value = uri
                    Log.d("GeoAR", "Captura guardada: $uri")
                } else {
                    Log.e("GeoAR", "La captura falló")
                }
            }
        }

    }

    fun saveImageToGallery(context: Context, sourceUri: Uri) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(sourceUri) ?: return

        val filename = "AR_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/ARCaptures"
            )
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (imageUri != null) {
            contentResolver.openOutputStream(imageUri).use { outputStream ->
                inputStream.copyTo(outputStream!!)
            }

            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            contentResolver.update(imageUri, contentValues, null, null)

            Toast.makeText(context, "Imagen guardada en galería", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
        }
    }
}
