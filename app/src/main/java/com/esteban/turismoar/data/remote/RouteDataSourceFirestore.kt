package com.esteban.turismoar.data.remote

import com.esteban.turismoar.domain.models.GeoPoint
import com.esteban.turismoar.domain.models.Route
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RouteDataSourceFirestore(private val db: FirebaseFirestore) {

    suspend fun getRoutes(): List<Route> {
        val result = db.collection("rutes").get().await()
        return result.map { document ->
//            Log.d("FirestoreData", document.get("geoPoints").toString())
            val geoPoints = (document.get("geoPoints") as? List<Map<String, Any>>)?.map {
                GeoPoint(
                    latitude = it["latitude"] as Double,
                    longitude = it["longitude"] as Double,
                    name = it["name"] as String,
                    model = it["model"] as String ?: "",
                    description = it["description"] as String ?: ""
                )
            } ?: emptyList()

            Route(
                id = document.getLong("id")?.toInt() ?: 0,
                title = document.getString("title") ?: "",
                description = document.getString("description") ?: "",
                imageUrl = document.getString("imageUrl") ?: "",
                type = document.getString("type") ?: "",
                geoPoints = geoPoints,
                schedules = document.getString("schedules") ?: "",
                promotion = document.getString("promotion") ?: "",
                phone = document.getString("phone") ?: "",
                webSite = document.getString("webSite") ?: ""
            )
        }
    }
}
