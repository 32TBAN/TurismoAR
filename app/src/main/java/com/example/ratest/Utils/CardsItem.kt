package com.example.ratest.Utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ratest.R

data class CardItem(
    val title: String,
    val description: String = "",
    val imageRes: Int = R.drawable.monumento
)

@Composable
fun getCards(): List<List<CardItem>> {
    val cardsRutes = listOf(
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes1),
            description = stringResource(id = R.string.cart_description_tourist_routes1)
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes2),
            description = stringResource(id = R.string.cart_description_tourist_routes2)
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes3),
            description = stringResource(id = R.string.cart_description_tourist_routes3)
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes4),
            description = stringResource(id = R.string.cart_description_tourist_routes4)
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes4),
            description = stringResource(id = R.string.cart_description_tourist_routes4)
        )
    )

    val cardsPlaces = listOf(
        CardItem(
            title = stringResource(id = R.string.cart_description_places_of_interest1),
            description = "Descripción del lugar 1"
        ),
        CardItem(
            title = stringResource(id = R.string.cart_description_places_of_interest2),
            description = "Descripción del lugar 2"
        ),
        CardItem(
            title = stringResource(id = R.string.cart_description_places_of_interest3),
            description = ""
        ),
        CardItem(
            title = stringResource(id = R.string.cart_description_places_of_interest4),
            description = ""
        ),
        CardItem(
            title = stringResource(id = R.string.cart_description_places_of_interest5),
            description = ""
        )
    )

    val cardsEvents = listOf(
        CardItem(title = stringResource(id = R.string.cart_description_events1), description = ""),
        CardItem(title = stringResource(id = R.string.cart_description_events2), description = ""),
        CardItem(
            title = stringResource(id = R.string.cart_description_events3),
            description = "Descripción del evento 3"
        )
    )

    return listOf(cardsRutes, cardsPlaces, cardsEvents)
}
