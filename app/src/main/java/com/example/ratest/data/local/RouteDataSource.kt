package com.example.ratest.data.local

import com.example.ratest.domain.models.Route

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class RouteDataSource(private val context: Context) {

    suspend fun getRoutes(): List<Route>  = withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("Json/routes.json")
            .bufferedReader().use { it.readText() }

        Json.decodeFromString(jsonString)
    }
}
