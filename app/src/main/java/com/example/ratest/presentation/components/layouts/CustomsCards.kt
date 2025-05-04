package com.example.ratest.presentation.components.layouts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        if (title != "") {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .animateContentSize()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (scrollDirection == "horizontal") {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (type == "Info") {
                    routes.forEachIndexed { index, item ->
                        InfoCard(title = item.title, description = item.description,
                            onClick = {
                                viewModel.selectRouteById(item.id)
                                navController.navigate(DetalleScreen(item.id))
                            })
                    }
                } else {
                    routes.forEachIndexed {index, item ->
                        SmallCard(title = item.title, onClick = {
                            viewModel.selectRouteById(item.id)
                            navController.navigate(DetalleScreen(item.id))
                        }, icon = item.icon)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (type == "Info") {
                    routes.forEachIndexed { index, item ->
                        InfoCard(title = item.title, description = item.description, onClick = {
                            viewModel.selectRouteById(item.id)
                            navController.navigate(DetalleScreen(item.id))
                        })
                    }
                } else {
                    routes.forEachIndexed { index, item ->
                        SmallCard(title = item.title, onClick = {
                            viewModel.selectRouteById(item.id)
                            navController.navigate(DetalleScreen(item.id))
                        })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
