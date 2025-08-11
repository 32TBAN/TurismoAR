package com.esteban.turismoar.presentation.components.layouts

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esteban.turismoar.presentation.mappers.UiRoute

enum class ScrollDirection { Horizontal, Vertical }

enum class CardType { Small, Info }

@Composable
fun SectionCards(
    title: String = "",
    routes: List<UiRoute>,
    cardType: CardType = CardType.Small,
    scrollDirection: ScrollDirection = ScrollDirection.Horizontal,
    onRouteClick: (UiRoute) -> Unit
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

        if (scrollDirection == ScrollDirection.Horizontal) {
            val listState = rememberLazyListState()
            val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

            LazyRow(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(routes) { _, item ->
                    when (cardType) {
                        CardType.Info -> InfoCard(
                            title = item.title,
                            description = item.description,
                            onClick = { onRouteClick(item) },
                            icon = item.icon
                        )

                        CardType.Small -> SmallCard(
                            title = item.title,
                            icon = item.icon,
                            onClick = { onRouteClick(item) }
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
                    when (cardType) {
                        CardType.Info -> InfoCard(
                            title = item.title,
                            description = item.description,
                            onClick = { onRouteClick(item) },
                           icon = item.icon
                        )

                        CardType.Small -> SmallCard(
                            title = item.title,
                            icon = item.icon,
                            onClick = { onRouteClick(item) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSectionCards() {
    val dummyRoutes = listOf(
        UiRoute(1, "Ruta 1", "Desc", 0, "ruta", emptyList(), Icons.Default.LocationOn),
        UiRoute(2, "Ruta 2", "Desc", 0, "ruta", emptyList(), Icons.Default.LocationOn)
    )

    SectionCards(
        title = "Rutas Ejemplo",
        routes = dummyRoutes,
        cardType = CardType.Small,
        scrollDirection = ScrollDirection.Horizontal,
        onRouteClick = {}
    )
}
