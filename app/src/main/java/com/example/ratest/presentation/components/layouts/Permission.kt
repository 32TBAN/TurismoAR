package com.example.ratest.presentation.components.layouts

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun CheckLocationPermission(onGranted: @Composable () -> Unit, onDenied: @Composable () -> Unit = {}) {
    val context = LocalContext.current
    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    val permissionGranted = rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
    }

    LaunchedEffect(true) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            permissionGranted.value = true
        } else {
            launcher.launch(permission)
        }
    }

    if (permissionGranted.value) {
        onGranted()
    } else {
        onDenied()
    }
}