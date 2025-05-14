package com.example.ratest.presentation.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.ratest.presentation.mappers.UiRoute
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import com.example.ratest.ui.theme.White
import androidx.compose.material.icons.filled.ArrowBackIosNew
import com.example.ratest.presentation.components.buttons.ExploreARButton
import com.example.ratest.ui.theme.DarkGreen
import androidx.compose.foundation.layout.padding
import androidx.core.net.toUri
import com.example.ratest.presentation.navigation.RARScreen
import com.google.ar.core.ArCoreApk

@Composable
fun TopImageSection(route: UiRoute, navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Image(
            painter = painterResource(id = route.imageRes),
            contentDescription = "Imagen de ${route.title}",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(DarkGreen.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Volver atrás",
                tint = White,
                modifier = Modifier.size(28.dp)
            )
        }

        ExploreARButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 20.dp),
            onClick = {
                checkARSupportAndNavigate(context, route.id, route.type, navController)
            }
        )
    }
}

fun checkARSupportAndNavigate(
    context: Context,
    routeId: Int,
    routeType: String,
    navController: NavController
) {
    when (ArCoreApk.getInstance().checkAvailability(context)) {
        ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> {
            Toast.makeText(
                context,
                "Tu dispositivo no es compatible con Realidad Aumentada",
                Toast.LENGTH_LONG
            ).show()
        }

        ArCoreApk.Availability.SUPPORTED_INSTALLED -> {
            try {
                navController.navigate(RARScreen(routeId, routeType))
            }catch (e: Exception){
                Toast.makeText(context, "Error al cargar la ruta", Toast.LENGTH_LONG).show()
            }
        }

        else -> {
            Toast.makeText(
                context,
                "Por favor instala ARCore desde Google Play",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=com.google.ar.core".toUri()
            )
            context.startActivity(intent)
        }
    }
}
