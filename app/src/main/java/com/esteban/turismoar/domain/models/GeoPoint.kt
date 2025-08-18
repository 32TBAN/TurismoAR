package com.esteban.turismoar.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    var latitude: Double,
    var longitude: Double,
    var name: String,
    var model: String,
    var description: String? = ""
)
