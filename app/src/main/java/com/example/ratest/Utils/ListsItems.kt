package com.example.ratest.Utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ratest.R

data class CardItem(
    val title: String,
    val description: String = "",
    val imageRes: Int = R.drawable.monumento,
    val rute: String = "",
    val tipe: String = "ruta",
    val geoPoints: List<Triple<Double, Double, String>> = emptyList()
)

@Composable
fun getCards(): List<List<CardItem>> {
    val cardsRutes = listOf(
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes1),
            description = stringResource(id = R.string.cart_description_tourist_routes1),
            imageRes = R.drawable.comidas,
            rute = "comidas",
            geoPoints = listOf(
                Triple(-1.0440176, -78.5907475, "Helados"),
                Triple(-1.0440112, -78.5904399, ""),
                Triple(-1.043231, -78.590489, ""),
                Triple(-1.0432587, -78.5899706, "Hornado"),
                Triple(-1.043082, -78.588926, ""),
                Triple(-1.043201, -78.588422, "Confiteria (Humas, Quimbolitos)")
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes2),
            description = stringResource(id = R.string.cart_description_tourist_routes2),
            imageRes = R.drawable.monumentos,
            rute = "monumentos",
            geoPoints = listOf(
                Triple(
                    -1.043688,
                    -78.591043,
                    stringResource(id = R.string.cart_title_places_of_interest2)
                ),
                Triple(-1.0440112, -78.5904399, ""),
                Triple(
                    -1.045560,
                    -78.590215,
                    stringResource(id = R.string.cart_title_places_of_interest1)
                )
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes3),
            description = stringResource(id = R.string.cart_description_tourist_routes3),
            imageRes = R.drawable.plazas,
            rute = "plazas",
            geoPoints = listOf(
                Triple(-1.046304, -78.593458, "Plaza Agusto Dávalos"),
                Triple(-1.045852, -78.593208, ""),
                Triple(-1.044253, -78.593392, ""),
                Triple(-1.044032, -78.591116, ""),
                Triple(-1.043684, -78.590821, "Parque Central"),
                Triple(-1.043231, -78.590489, ""),
                Triple(-1.043082, -78.588926, ""),
                Triple(-1.043098, -78.588930, "Mercado Central"),
                Triple(-1.047000, -78.588446, ""),
                Triple(-1.047127, -78.588209, "Plaza San Antonío")
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes4),
            description = stringResource(id = R.string.cart_description_tourist_routes4),
            imageRes = R.drawable.transporte,
            rute = "transportes"
        )
    )

    val cardsPlaces = listOf(
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest1),
            description = stringResource(id = R.string.cart_description_places_of_interest1),
            imageRes = R.drawable.monumento_madre_ai,
            rute = "monumento_madre",
            tipe = "marcador",
            geoPoints = listOf(
                Triple(
                    -1.045560,
                    -78.590215,
                    stringResource(id = R.string.cart_title_places_of_interest1)
                )
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest2),
            description = stringResource(id = R.string.cart_description_places_of_interest2),
            imageRes = R.drawable.monumento_fray_ia,
            rute = "monumento_fray",
            tipe = "marcador",
            geoPoints = listOf(
                Triple(
                    -1.043688,
                    -78.591043,
                    stringResource(id = R.string.cart_title_places_of_interest2)
                )
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest3),
            description = stringResource(id = R.string.cart_description_places_of_interest3),
            imageRes = R.drawable.palacio_municipal,
            rute = "palacio_municipal",
            tipe = "marcador",
            geoPoints = listOf(
                Triple(-1.0440112, -78.5904399, stringResource(id = R.string.cart_title_places_of_interest3))
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest4),
            description = stringResource(id = R.string.cart_description_places_of_interest4),
            imageRes = R.drawable.iglesia_ai,
            rute = "iglesia",
            tipe = "marcador",
            geoPoints = listOf(
                Triple(-1.044253, -78.593392, stringResource(id = R.string.cart_title_places_of_interest4))
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest5),
            description = stringResource(id = R.string.cart_description_places_of_interest5),
            imageRes = R.drawable.coliseo_ai,
            rute = "coliseo",
            tipe = "marcador",
            geoPoints = listOf(
                Triple(-1.047000, -78.588446, stringResource(id = R.string.cart_title_places_of_interest5))
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest6),
            description = stringResource(id = R.string.cart_description_places_of_interest6),
            imageRes = R.drawable.mercado_ai,
            rute = "mercado",
            tipe = "marcador",
            geoPoints = listOf(
                Triple(-1.043098, -78.588930, stringResource(id = R.string.cart_title_places_of_interest6))
            )
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
fun getList(): List<Int> {
    val images = listOf(
        R.drawable.comidas,
        R.drawable.palacio_municipal,
        R.drawable.parque_ai,
        R.drawable.iglesia_ai,
        R.drawable.plazas,
        R.drawable.mercado_ai,
        R.drawable.transporte,
        R.drawable.coliseo_ai,
        R.drawable.monumento_fray_ia,
        R.drawable.monumento_madre_ai
    )
    return images
}