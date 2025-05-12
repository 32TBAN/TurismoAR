package com.example.ratest.presentation.components.layouts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratest.presentation.navigation.DetalleScreen
import com.example.ratest.presentation.mappers.UiRoute
import com.example.ratest.presentation.viewmodels.RouteViewModel

@Composable
fun SectionCards(
    title: String = "",
    routes: List<UiRoute>,
    type: String = "Small",
    scrollDirection: String = "horizontal",
    navController: NavController,
    viewModel: RouteViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .animateContentSize()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (scrollDirection == "horizontal") {
            val listState = rememberLazyListState()
            val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

            LazyRow(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(routes) { index, item ->
                    val onClick = {
                        viewModel.selectRouteById(item.id)
                        navController.navigate(DetalleScreen(item.id))
                    }
                    if (type == "Info") {
                        InfoCard(
                            title = item.title,
                            description = item.description,
                            onClick = onClick
                        )
                    } else {
                        SmallCard(
                            title = item.title,
                            icon = item.icon,
                            onClick = onClick
                        )
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                routes.forEach { item ->
                    val onClick = {
                        viewModel.selectRouteById(item.id)
                        navController.navigate(DetalleScreen(item.id))
                    }
                    if (type == "Info") {
                        InfoCard(
                            title = item.title,
                            description = item.description,
                            onClick = onClick
                        )
                    } else {
                        SmallCard(title = item.title, icon = item.icon, onClick = onClick)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


