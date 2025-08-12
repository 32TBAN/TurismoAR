package com.esteban.turismoar.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    var id: Int,
    var title: String,
    var description: String?,
    var imageUrl: String,
    var type: String,
    var geoPoints: List<GeoPoint>,
    var schedules: String? = null,
    var promotion: String? = null,
    var phone: String? = null,
    var webSite: String? = null
)
