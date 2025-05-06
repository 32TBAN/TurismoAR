package com.example.ratest.presentation.components.layouts

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.presentation.navigation.RARScreen
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.Green
import com.example.ratest.ui.theme.White
import com.google.ar.core.ArCoreApk
import android.widget.Toast
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.ratest.presentation.viewmodels.RouteViewModel
import com.example.ratest.ui.theme.Blue
import androidx.core.net.toUri
import com.example.ratest.presentation.mappers.toUiRoute

@Composable
fun DetalleInfo(
    navController: NavController,
    listState: LazyListState = rememberLazyListState(),
    viewModel: RouteViewModel
) {
    val context = LocalContext.current
    val route = viewModel.selectedRoute.collectAsState().value?.toUiRoute(context) ?: return

    route.let {
        LazyColumn(state = listState) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Image(
                        painter = painterResource(id = it.imageRes),
                        contentDescription = it.title,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(horizontal = 6.dp, vertical = 16.dp)
                            .background(
                                DarkGreen.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(50)
                            )
                            .border(
                                1.dp,
                                DarkGreen.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(50)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Regresar",
                            tint = White,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            // Verifica si AR está disponible en el dispositivo
                            when (ArCoreApk.getInstance().checkAvailability(context)) {
                                ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> {
                                    Toast.makeText(
                                        context,
                                        "Tu dispositivo no es compatible con Realidad Aumentada",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                ArCoreApk.Availability.SUPPORTED_INSTALLED -> {
                                    // Dispositivo compatible con AR
                                    navController.navigate(RARScreen(it.id, it.type))
                                }

                                else -> {
                                    Toast.makeText(
                                        context,
                                        "Realidad aumentada no disponible, por favor instala ARCore",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        "https://play.google.com/store/apps/details?id=com.google.ar.core".toUri()
                                    )

                                    if (intent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(intent)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .width(230.dp)
                            .align(Alignment.BottomEnd)
                            .align(Alignment.BottomEnd)
                            .offset(y = 30.dp)  // Desplazar el botón hacia abajo para que se vea parcialmente fuera de la imagen
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Blue,
                                        Green
                                    )
                                )
                            )
                            .padding(horizontal = 7.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Explorar con RA",
                                tint = White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Explorar con Realidad Aumentada", color = White,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(width = 170.dp, height = 24.dp)
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                Text(
                    text = it.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

            }

            item {
                Text(
                    text = it.description,
                    style = MaterialTheme.typography.body2,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MapSection(
                    title = {
                        Text(
                            "Ruta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = DarkGreen
                        )
                    },
                    geoPoints = it.geoPoints,
                    zoomLevel = 17.0,
                    type = it.type,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}
