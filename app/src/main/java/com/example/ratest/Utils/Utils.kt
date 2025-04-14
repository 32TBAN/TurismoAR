package com.example.ratest.Utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode

object Utils {

    fun getModel(modelname: String): String {
        Log.e("nombre modelo", modelname)
        return when (modelname) {
            "comidas" -> "models/frostmourne_with_animations.glb"
            "monumentos" -> "models/helm_of_domination.glb"
            "plazas" -> "models/anime_fox_girl.glb"
            "transportes" -> "models/warcraft_draenei_fanart.glb"
            "5" -> "models/aqua__anime_chibi_model.glb"
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
        if (model.isNullOrEmpty()) {
            Log.e("ARScreen", "Model path is null or empty!")
        }
        if (modelLoader == null) {
            Log.e("ARScreen", "ModelLoader is null!")
        }


        val anchorNode = AnchorNode(engine = engine, anchor = anchor)
        val modelNode = ModelNode(
            modelInstance = modelInstance.apply {
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
}