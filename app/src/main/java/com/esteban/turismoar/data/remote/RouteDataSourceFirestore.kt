package com.esteban.turismoar.data.remote

import android.util.Log
import com.esteban.turismoar.domain.models.Route
import com.google.firebase.firestore.FirebaseFirestore

class RouteDataSourceFirestore {
    val db = FirebaseFirestore.getInstance()

    fun getRoutes(): List<Route> {
        val rutas = mutableListOf<Route>()
        db.collection("rutes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val route = document.toObject(Route::class.java)
                    rutas.add(route)
                }
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
        return rutas
    }
}