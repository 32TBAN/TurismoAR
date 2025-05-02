package com.example.ratest.Utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ratest.R
import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val name: String
)

data class CardItem(
    val title: String,
    val description: String = "",
    val imageRes: Int = R.drawable.monumento,
    val rute: String = "",
    val type: String = "ruta",
    var geoPoints: List<GeoPoint> = emptyList()
    //todo agregar icono, nose si es necesario aqui.
)

@Composable
fun getCards(): List<List<CardItem>> {
    val cardsRutes = listOf(
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes1),
            description = stringResource(id = R.string.cart_description_tourist_routes1),
            imageRes = R.drawable.comidas,
            rute = "comida",
            geoPoints = listOf(
                GeoPoint(-1.0440176, -78.5907475, "Helados"),
                GeoPoint(-1.0440112, -78.5904399, ""),
                GeoPoint(-1.043231, -78.590489, ""),
                GeoPoint(-1.0432587, -78.5899706, "Hornado"),
                GeoPoint(-1.043082, -78.588926, ""),
                GeoPoint(-1.043201, -78.588422, "Confiteria (Humas, Quimbolitos)")
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes2),
            description = stringResource(id = R.string.cart_description_tourist_routes2),
            imageRes = R.drawable.monumentos,
            rute = "monumentos",
            geoPoints = listOf(
                GeoPoint(
                    -1.043688,
                    -78.591043,
                    stringResource(id = R.string.cart_title_places_of_interest2)
                ),
                GeoPoint(-1.0440112, -78.5904399, ""),
                GeoPoint(
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
                GeoPoint(-1.046304, -78.593458, "Plaza Agusto Dávalos"),
                GeoPoint(-1.045852, -78.593208, ""),
                GeoPoint(-1.044253, -78.593392, ""),
                GeoPoint(-1.044032, -78.591116, ""),
                GeoPoint(-1.043684, -78.590821, "Parque Central"),
                GeoPoint(-1.043231, -78.590489, ""),
                GeoPoint(-1.043082, -78.588926, ""),
                GeoPoint(-1.043098, -78.588930, "Mercado Central"),
                GeoPoint(-1.047000, -78.588446, ""),
                GeoPoint(-1.047127, -78.588209, "Plaza San Antonío")
            )
        ),
        //no solo prueba
        CardItem(
            title = stringResource(id = R.string.cart_title_tourist_routes4),
            description = stringResource(id = R.string.cart_description_tourist_routes4),
            imageRes = R.drawable.transporte,
            rute = "transportes",
            geoPoints = listOf(
                GeoPoint(-1.016265, -78.565139, "Punto 1"),
                GeoPoint(-1.016227, -78.565158, "Punto 2"),
                GeoPoint(-1.016179, -78.565177, ""),
                GeoPoint(-1.016179, -78.565177, ""),
                //pruebas U
//                GeoPoint(-1.267947,-78.624075, "Punto 1"),
//                GeoPoint(-1.267857, -78.624126, "Punto 2")
            )
        )
    )

    val cardsPlaces = listOf(
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest1),
            description = stringResource(id = R.string.cart_description_places_of_interest1),
            imageRes = R.drawable.monumento_madre_ai,
            rute = "monumento_madre",
            type = "marcador",
            geoPoints = listOf(
                GeoPoint(
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
            type = "marcador",
            geoPoints = listOf(
                GeoPoint(
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
            type = "marcador",
            geoPoints = listOf(
                GeoPoint(-1.0440112, -78.5904399, stringResource(id = R.string.cart_title_places_of_interest3))
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest4),
            description = stringResource(id = R.string.cart_description_places_of_interest4),
            imageRes = R.drawable.iglesia_ai,
            rute = "iglesia",
            type = "marcador",
            geoPoints = listOf(
//                GeoPoint(-1.044253, -78.593392, stringResource(id = R.string.cart_title_places_of_interest4))
                //Pruebas
                        GeoPoint(-1.016230, -78.565194, stringResource(id = R.string.cart_title_places_of_interest4))

            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest5),
            description = stringResource(id = R.string.cart_description_places_of_interest5),
            imageRes = R.drawable.coliseo_ai,
            rute = "coliseo",
            type = "marcador",
            geoPoints = listOf(
                GeoPoint(-1.047000, -78.588446, stringResource(id = R.string.cart_title_places_of_interest5))
            )
        ),
        CardItem(
            title = stringResource(id = R.string.cart_title_places_of_interest6),
            description = stringResource(id = R.string.cart_description_places_of_interest6),
            imageRes = R.drawable.mercado_ai,
            rute = "mercado",
            type = "marcador",
            geoPoints = listOf(
                GeoPoint(-1.043098, -78.588930, stringResource(id = R.string.cart_title_places_of_interest6))
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
fun getGeoPointsForRoute(rute: String): List<GeoPoint> {
    val cards = getCards()
    val allCards = cards.flatten()

    return allCards.firstOrNull { it.rute == rute }?.geoPoints ?: emptyList()
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