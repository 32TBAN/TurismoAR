package com.example.ratest.presentation.Screens

import androidx.navigation.NavController

@composable()
fun ARScreen(navController: NavController, moddel: String){
    val engine = rememberEngine()
    val modelLoder = rememberModelLoader(engine = engine)
    val materialLoader = rememberMaterialLoader(engine = engine)
    val cameraNodes = rememberARCameraNode(engine = engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine = engine)
    val collisionSystem = rememberCollisionSystem(view = view)
    val planeRender = remember {
        mutableStateOf(true)
    }
    val modelInstance = remember {
        mutableListOf<ModelInstance>()
    }
    val trackingFailureReason = remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    val frame = remember {
        mutableStateOf<Frame?>(null)
    }
    ARScreen(
        modifier = Modifier.fillMaxSize(),
        childNodes = childNodes
    )
}