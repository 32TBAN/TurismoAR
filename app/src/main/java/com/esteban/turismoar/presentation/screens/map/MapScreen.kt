package com.esteban.turismoar.presentation.screens.map

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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.presentation.components.layouts.InfoCard
import com.esteban.turismoar.ui.theme.Green
import com.esteban.turismoar.ui.theme.White
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )

    var selectPints by remember { mutableStateOf(listOf<GeoPoint>()) }
    val listState = rememberLazyListState()

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

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
                    if (selectPints.isEmpty()) {
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
                    } else {
                        selectPints.forEach { geoPoint ->
                            InfoCard(geoPoint.name, geoPoint.description)
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

            )
        }
    }
}
