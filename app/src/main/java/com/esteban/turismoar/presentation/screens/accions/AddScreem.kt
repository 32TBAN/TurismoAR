package com.esteban.turismoar.presentation.screens.accions

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.esteban.turismoar.presentation.components.image.ImagePicker
import com.esteban.turismoar.presentation.components.inputs.InputTextField
import com.esteban.turismoar.presentation.components.inputs.Select
import com.esteban.turismoar.presentation.components.layouts.map.MapSection
import com.esteban.turismoar.presentation.navigation.MapScreen
import com.esteban.turismoar.ui.theme.DarkGreen
import com.esteban.turismoar.ui.theme.Green
import com.esteban.turismoar.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(navController: NavController) {
    val listState = rememberLazyListState()
    val isScrolled = listState.firstVisibleItemScrollOffset > 0

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(White).border(1.dp, White),
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
                                "Add a place",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(48.dp).border(1.dp, Green))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isScrolled) Color.Gray.copy(0.1f) else White,
                )
            )
        }
    )
    { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    "Place details",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    "Provide us with the following information:",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                InputTextField(placeholder = "Name")
                var options = listOf("Rute", "Mark", "Event", "Business")
                Select(options = options, placeholder = "Category")
                InputTextField(placeholder = "Description")
                ImagePicker(title = "Edit image for the place", onImageSelected = { uri ->
                    //Todo Aquí puedes subir la imagen a Firebase Storage o guardarla
                    Log.d("ImagePicker", "Imagen seleccionada: $uri")
                })
                MapPreview({
                    navController.navigate(MapScreen)
                }, title = "Edit map for the place")
            }
        }
    }
}

@Composable
fun MapPreview(onClick: () -> Unit, title: String? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray).border(1.dp, DarkGreen, RoundedCornerShape(8.dp))
    ) {
        MapSection(zoomLevel = 15.5, controls = false)
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { onClick() }
        )
        title?.let {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(White)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Green,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 4.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = it,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 15.sp,
                    color = DarkGreen
                )
            }
        }
    }
}


