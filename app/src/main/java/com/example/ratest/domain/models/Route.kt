package com.example.ratest.domain.models

data class Route(
    val id: Int,
    val title: String,
    val description: String,
    val imageRes: String,
    val type: String,
    val geoPoints: List<GeoPoint>
)
