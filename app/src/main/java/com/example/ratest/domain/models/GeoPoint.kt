package com.example.ratest.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val model: String
)
