package com.esteban.turismoar.presentation.screens.map

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.esteban.turismoar.presentation.components.layouts.map.MapSection
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.presentation.components.inputs.InputTextField
import com.esteban.turismoar.ui.theme.DarkGreen
import com.esteban.turismoar.ui.theme.Green
import com.esteban.turismoar.ui.theme.White
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import com.esteban.turismoar.presentation.components.buttons.ColorButton
import com.esteban.turismoar.presentation.components.buttons.CustomButton
import com.esteban.turismoar.presentation.components.buttons.VariantButton
import com.esteban.turismoar.presentation.components.layouts.InfoCard
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )
    var selectPints by remember { mutableStateOf(listOf<GeoPoint>()) }
    val listState = rememberLazyListState()
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)
    var geoPointActied by remember { mutableStateOf<GeoPoint?>(null) }
//    val showModelLoader = remember { mutableStateOf(false) }
    LaunchedEffect(geoPointActied) {
        if (geoPointActied != null) {
            scaffoldState.bottomSheetState.expand()
        } else {
            scaffoldState.bottomSheetState.show()
        }
    }
    BottomSheetScaffold(
        topBar = {
            // Ocultar si la hoja está expandida completamente
            if (bottomSheetState.currentValue != SheetValue.Expanded) {
                TopAppBar(
                    modifier = Modifier
                        .background(White)
                        .border(1.dp, White),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Outlined.Close, contentDescription = "Close")
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Choose the place",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(
                                modifier = Modifier
                                    .width(48.dp)
                                    .border(1.dp, Green)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
                )
            }
        },
        sheetPeekHeight = 30.dp,
        sheetContent = {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                item {
                    geoPointActied?.let {
                        Row {
                            Text("Coord. X: ", fontWeight = FontWeight.Bold)
                            Text("${it.latitude}")
                        }
                        Row {
                            Text("Coord. Y: ", fontWeight = FontWeight.Bold)
                            Text("${it.longitude}")
                        }
                        InputTextField(
                            placeholder = "Name",
                            onValueChange = { text -> it.name = text })
                        InputTextField(
                            placeholder = "Description",
                            onValueChange = { text -> it.description = text })
                        Model3DViewer(onModelSelected = { uri ->
                            it.model = uri.toString()
                        })
                        Row(modifier = Modifier.fillMaxWidth()) {
                            CustomButton(
                                text = "Save",
                                onClick = {
                                    selectPints = selectPints + geoPointActied!!
                                    geoPointActied = null
                                },
                                modifier = Modifier.weight(1f),
                                color = ColorButton.Success,
                                icon = Icons.Outlined.Save,
                                variant = VariantButton.Bordered
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            CustomButton(
                                text = "Cancel",
                                onClick = {
                                    geoPointActied = null
                                },
                                modifier = Modifier.weight(1f),
                                color = ColorButton.Danger,
                                icon = Icons.Outlined.Close,
                                variant = VariantButton.Bordered
                            )
                        }
                    } ?: run {
                        if (selectPints.isNotEmpty()) {
                            selectPints.forEach {
                                InfoCard(title = it.name, description = it.description)
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Has not selected any points yet",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(16.dp),
                                    color = Green,
                                )

                            }
                            Spacer(modifier = Modifier.height(18.dp))
                        }

                    }
                }
            }
        },
        sheetContainerColor = Color.White,
        scaffoldState = scaffoldState
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapSection(
                modifier = Modifier.fillMaxSize(),
                zoomLevel = 15.5,
                onTouch = { point ->
                    geoPointActied = point
                }
            )
        }
//        if (showModelLoader.value) {
//            CustomButton(text = "Cerrar",
//                onClick = {showModelLoader.value = false})
//            Model3DViewer()
//        }
    }
}

@Composable
fun Model3DViewer(onModelSelected: (Uri) -> Unit) {
    var selectedModelUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                selectedModelUri = it
                onModelSelected(it)
            }
        }
    )

    CustomButton(
        text = "Upload Model 3D", onClick = {
            launcher.launch("*/*")
        },
        icon = Icons.Outlined.Upload,
        modifier = Modifier.fillMaxWidth(),
        variant = VariantButton.Bordered,
        color = ColorButton.Primary
    )
    Spacer(modifier = Modifier.height(8.dp))
    AndroidView(
        factory = { ctx ->
            SceneView(ctx)
        },
        update = { sceneView ->
            sceneView.clearChildNodes()
            val modeLoader = sceneView.modelLoader
            val modelInstance = if (selectedModelUri != null) {
                val file = uriToFile(context, selectedModelUri!!)
                modeLoader.createModelInstance(file)
            } else {
                modeLoader.createModelInstance("models/navigation_pin.glb")
            }

            modelInstance.let {
                val modelNode = ModelNode(it, scaleToUnits = 1.0f).apply {
                    rotation = Rotation(x = 0f, y = 65f, z = 0f)
                }

                sceneView.addChildNode(modelNode)
                sceneView.cameraNode.position = Position(x = 0f, y = 0f, z = 3f)
                sceneView.cameraNode.lookAt(modelNode)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, DarkGreen, RoundedCornerShape(8.dp))
    )
    Spacer(modifier = Modifier.height(8.dp))

}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val tempFile = File(context.cacheDir, "temp_model.glb")
    inputStream.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}
