package com.esteban.turismoar.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val type: String,
    val geoPoints: List<GeoPoint>,
    val schedules: String? = null,
    val promotion: String? = null,
    val phone: String? = null,
    val webSite: String? = null
)
