package com.example.ratest.presentation.viewmodels.ar

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratest.domain.models.GeoPoint
import com.example.ratest.domain.usecase.TourManager
import com.example.ratest.presentation.components.layouts.ar.ArNodeFactory
import com.example.ratest.presentation.viewmodels.TourUIState
import com.example.ratest.utils.ErrorState
import com.example.ratest.utils.Utils
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Earth
import com.google.ar.core.Frame
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.NotTrackingException
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.abs

class ARViewModel(
    private val tourManager: TourManager
) : ViewModel() {
    private val uiStateMutable = MutableStateFlow<TourUIState>(TourUIState.Loading)
    val uiState: StateFlow<TourUIState> = uiStateMutable

    private val distanceTextMutable = MutableStateFlow("0.0 m")
    val distanceText: StateFlow<String> = distanceTextMutable

    val selectedModelPath = mutableStateOf<String?>(null)
    val scaleModel = mutableFloatStateOf(0.6f)
    var imageUriState = mutableStateOf<Uri?>(null)

    val arNodes = mutableStateListOf<Node>()

    @SuppressLint("StaticFieldLeak")
    var arSceneView: ARSceneView? = null


    private val isPinCreated = MutableStateFlow(false)
    private var stableTrackingFrames = 0
    private val requiredStableFrames = 10
    var currentPosition = mutableStateOf<GeoPoint?>(GeoPoint(0.0, 0.0, "Posición actual", "", ""))
    var isTrackingStable = mutableStateOf(false)

    val visibleRange = 5.99
    val modelInstanceList = mutableListOf<ModelInstance>()
    val errorState = ErrorState()

    val targetPosition = mutableStateOf<Float3?>(null)

    fun initialize(geoPoints: List<GeoPoint>) {
        tourManager.setGeoPoints(geoPoints)

        viewModelScope.launch {
            try {
                tourManager.loadVisitedPoints()
                uiStateMutable.value = TourUIState.InProgress(
                    target = tourManager.getNextTarget(
                        currentPosition.value?.latitude ?: 0.0,
                        currentPosition.value?.longitude ?: 0.0
                    ) ?: GeoPoint(
                        0.0,
                        0.0,
                        "Sin destino",
                        "", ""
                    )
                )
//                Log.d("GeoAR", "Initialized with target: ${uiStateMutable.value}")
            } catch (e: Exception) {
                uiStateMutable.value = TourUIState.Error(e.localizedMessage ?: "Error desconocido")
                errorState.setError(e)
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
                    targetPosition.value = null
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

                val firstTarget = tourManager.getNextTarget(
                    currentPosition.value?.latitude ?: 0.0,
                    currentPosition.value?.longitude ?: 0.0
                ) ?: tourManager.getNextTarget(0.0, 0.0)
                if (firstTarget != null) {
                    uiStateMutable.value = TourUIState.InProgress(firstTarget)
                } else {
                    uiStateMutable.value = TourUIState.Error("No hay destinos disponibles.")
                }

                arSceneView?.removeChildNodes(arNodes)
                arNodes.clear()
                isPinCreated.value = false
            } catch (e: Exception) {
                uiStateMutable.value = TourUIState.Error(e.message ?: "Error desconocido")
                errorState.setError(e)
            }
        }
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

            val deltaLat =
                abs((currentPosition.value?.latitude ?: geoPose.latitude) - geoPose.latitude)
            val deltaLon =
                abs((currentPosition.value?.longitude ?: geoPose.longitude) - geoPose.longitude)

            if (deltaLat < 0.00001 && deltaLon < 0.00001) {
                stableTrackingFrames++
            } else {
                stableTrackingFrames = 0
            }

            currentPosition.value?.latitude = geoPose.latitude
            currentPosition.value?.longitude = geoPose.longitude

            if (stableTrackingFrames >= requiredStableFrames) {
                isTrackingStable.value = true
            } else {
                uiStateMutable.value = TourUIState.Loading
                return
            }

            if (earth.trackingState == TrackingState.TRACKING) {
                currentPosition.value?.latitude = geoPose.latitude
                currentPosition.value?.longitude = geoPose.longitude
                val distance = tourManager.haversineDistance(
                    geoPose.latitude,
                    geoPose.longitude,
                    currentTarget.latitude,
                    currentTarget.longitude
                )

                distanceTextMutable.value = if (distance >= 1000) {
                    val km = distance / 1000
                    "${"%.2f".format(km)} km"
                } else {
                    "${"%.0f".format(distance)} m"
                }

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
                    targetPosition.value = Float3(
                        anchor.pose.tx(),
                        anchor.pose.ty(),
                        anchor.pose.tz()
                    )
//                    val anchorLatLng = earth.getGeospatialPose(anchor.pose).let {
//                        val lat = it.latitude
//                        val lng = it.longitude
//                        val alt = it.altitude
//
//                        Triple(lat, lng, alt)
//                    }

//                    Log.d("GeoAR", "Anchor created at: $anchorLatLng")
//                    val adjustedTargetPosition = Float3(
//                        anchorLatLng.first.toFloat(),
//                        geoPose.longitude.toFloat(),
//                        geoPose.altitude.toFloat()
//                    )
                    val pinNode = ArNodeFactory.createAnchorNode(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstance = modelInstanceList,
                        anchor = anchor,
                        model = currentTarget.model,
                        scaleToUnits = 2.6f
                    )

//                    pinNode.position = adjustedTargetPosition
                    pinNode.position = Float3(
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
                    targetPosition.value = null
                }
            }
        } catch (e: NotTrackingException) {
            uiStateMutable.value = TourUIState.Error(e.message ?: "Error desconocido")
            errorState.setError(e)
        } catch (e: Exception) {
            uiStateMutable.value = TourUIState.Error(e.message ?: "Error desconocido")
            errorState.setError(e)
        }
    }

    fun updateArrowNode(
        arrowNode: ModelNode,
        frame: Frame?,
        earth: Earth?,
    ) {
        frame?.camera?.pose?.let { pose ->
            val forwardDirection = Utils.quaternionToForward(pose.rotationQuaternion)
            val hudDistance = -0.2f
            val hudYOffset = 0.1f

            arrowNode.isVisible = selectedModelPath.value == null

            arrowNode.position = Float3(
                pose.tx() + forwardDirection.x * hudDistance,
                pose.ty() - hudYOffset,
                pose.tz() + forwardDirection.z * hudDistance
            )

            if (targetPosition.value == null) {
                val earth = earth ?: return
                val currentTarget = (uiStateMutable.value as? TourUIState.InProgress)?.target
                if (currentTarget == null) return

                val tempAnchor = earth.createAnchor(
                    currentTarget.latitude,
                    currentTarget.longitude,
                    earth.cameraGeospatialPose.altitude,
                    floatArrayOf(0f, 0f, 0f, 1f)
                )

                targetPosition.value = Float3(
                    tempAnchor.pose.tx(),
                    tempAnchor.pose.ty(),
                    tempAnchor.pose.tz()
                )
                tempAnchor.detach()
            }
            targetPosition.value?.let { target ->
                arrowNode.lookAt(target, Float3(0f, 1f, 0f))
            }
        }
    }

    fun getZisedVisitedPoints() = tourManager.getVisited()

    fun createModelNode(anchor: Anchor) {
        val nodeModel = ArNodeFactory.createAnchorNode(
            engine = arSceneView!!.engine,
            modelLoader = arSceneView!!.modelLoader,
            materialLoader = arSceneView!!.materialLoader,
            modelInstance = modelInstanceList,
            anchor = anchor,
            model = selectedModelPath.value!!,
            scaleToUnits = scaleModel.floatValue
        )
        arSceneView?.addChildNode(nodeModel)
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
            it.childNodes[0].isVisible = false
            captureARView(it) { bitmap ->
                if (bitmap != null) {
                    val uri = saveBitmapToCache(context, bitmap)
                    imageUriState.value = uri
                    Log.d("GeoAR", "Captura guardada: $uri")
                } else {
                    Log.e("GeoAR", "La captura falló")
                }
            }
            it.childNodes[0].isVisible = true
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

    val tutorialPages = listOf(
        "Pulsa el botón del mapa para ver tu ubicación actual.",
        "Explora tu entorno moviendo el teléfono. La flecha te guiará hasta el destino.",
        "Al llegar, verás un objeto con información sobre el lugar. Se marcará como visitado.",
        "Completa todos los puntos del recorrido para obtener tu logro.",
        "Elige un personaje y toma una foto divertida en realidad aumentada."
    )

    val pagesObjectTutorial = listOf(
        "Para colocar el objeto, toca la pantalla apuntando a una superficie plana los puntos te mostrarán si es posible colocarlo o no.",
        "Una vez colocado, puedes moverlo con un dedo y cambiarle el tamaño con dos dedos."
    )
}