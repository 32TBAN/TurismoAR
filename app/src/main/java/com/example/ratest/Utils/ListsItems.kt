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
            title = stringResource(id = R.string.cart_title_places_of_interest1),
            description = stringResource(id = R.string.cart_description_places_of_interest1),
            imageRes = R.drawable.monumento
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest2),
            description = stringResource(id = R.string.cart_description_places_of_interest2),
            imageRes = R.drawable.monumentofraysalcedo

        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest3),
            description = stringResource(id = R.string.cart_description_places_of_interest3),
            imageRes = R.drawable.palacio_municipal

        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest4),
            description = stringResource(id = R.string.cart_description_places_of_interest4),
            imageRes = R.drawable.iglesia
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest5),
            description = stringResource(id = R.string.cart_description_places_of_interest5),
            imageRes = R.drawable.coliseo
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest6),
            description = stringResource(id = R.string.cart_description_places_of_interest6),
            imageRes = R.drawable.mercado
        )
    )

    val cardsEvents = listOf(
        CardItem(
            title = stringResource(id = R.string.cart_title_events1),
            description = stringResource(id = R.string.cart_description_events1)
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_events2),
            description = stringResource(id = R.string.cart_description_events1)
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_events3),
            description = stringResource(id = R.string.cart_description_events1)
        )
    )

    return listOf(cardsRutes, cardsPlaces, cardsEvents)
}

@Composable
fun getList(): List<Int>{
    val images = listOf(
        R.drawable.parque,
        R.drawable.palacio_municipal,
        R.drawable.iglesia,
        R.drawable.mercado,
        R.drawable.coliseo,
        R.drawable.monumentofraysalcedo,
        R.drawable.monumento,

    )
    return images
}