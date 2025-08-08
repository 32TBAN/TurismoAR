package com.example.ratest.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    var latitude: Double,
    var longitude: Double,
    val name: String,
    val model: String,
    val description: String? = ""
)
