package com.esteban.turismoar.presentation.screens.detail


import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.esteban.turismoar.ui.theme.DarkGreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.esteban.turismoar.presentation.viewmodels.home.RouteViewModel
import com.esteban.turismoar.presentation.mappers.toUiRoute
import com.esteban.turismoar.presentation.components.TopImageSection
import com.esteban.turismoar.presentation.components.layouts.map.MapSection

@Composable
fun DetailInfo(
    navController: NavController,
    viewModel: RouteViewModel
) {
    val context = LocalContext.current
    val route = viewModel.selectedRoute.collectAsState().value?.toUiRoute(context) ?: return

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    val columnModifier = if (isLandscape) {
        Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 17.dp)
    } else {
        Modifier.padding(horizontal = 17.dp)
    }

    Column(modifier = columnModifier) {
        TopImageSection(route, navController)
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = route.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        route.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (route.type == "comercio") {
            route.schedules?.let {
                Text(
                    text = "🕒 Horario: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            route.promotion?.let {
                Text(
                    text = "🎉 Promoción: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            route.phone?.let {
                Text(
                    text = "📞 Teléfono: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            route.webSite?.let {
                Text(
                    text = "🌐 Sitio web: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = DarkGreen
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (isLandscape) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { 
                MapSection(
                    title = "Ruta",
                    geoPoints = route.geoPoints,
                    zoomLevel = 15.0,
                    type = route.type,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // Comportamiento normal en vertical
            MapSection(
                title = "Ruta",
                geoPoints = route.geoPoints,
                zoomLevel = 15.0,
                type = route.type,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
    }
}
