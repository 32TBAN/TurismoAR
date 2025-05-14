package com.example.ratest.presentation.components.layouts.ar

import android.content.Context
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.hardware.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.ratest.R
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White

@Composable
fun CompassOverlay() {
    val context = LocalContext.current
    var azimuth by remember { mutableFloatStateOf(0f) }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val sensorEventListener = remember {
        object : SensorEventListener {
            private var accelerometer = FloatArray(3)
            private var magnetometer = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> accelerometer = event.values
                    Sensor.TYPE_MAGNETIC_FIELD -> magnetometer = event.values
                }

                val R = FloatArray(9)
                val I = FloatArray(9)
                if (SensorManager.getRotationMatrix(R, I, accelerometer, magnetometer)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    val azimuthRadians = orientation[0]
                    val azimuthDegrees = Math.toDegrees(azimuthRadians.toDouble()).toFloat()
                    azimuth = (azimuthDegrees + 360) % 360
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(sensorEventListener, accel, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(sensorEventListener, magnetic, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Box(Modifier.fillMaxWidth().padding(horizontal =  17.dp, vertical = 8.dp), contentAlignment = Alignment.TopEnd){
        Surface(
            modifier = Modifier.size(40.dp),
            tonalElevation = 6.dp,
            shape = CircleShape,
            shadowElevation = 8.dp,
            color = Green.copy(alpha = 2.9f)
        ) {
            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .graphicsLayer {
                        rotationZ = -azimuth
                    },
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_compass),
                    contentDescription = "Compass Icon",
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    }
}
