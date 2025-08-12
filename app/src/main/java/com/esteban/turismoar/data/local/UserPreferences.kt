package com.esteban.turismoar.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {

    private val VISITED_POINTS_KEY = stringSetPreferencesKey("visited_points")

    fun getVisitedPoints(context: Context): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[VISITED_POINTS_KEY] ?: emptySet()
        }
    }

    suspend fun markPointAsVisited(context: Context, pointName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[VISITED_POINTS_KEY] ?: emptySet()
            preferences[VISITED_POINTS_KEY] = current + pointName
        }
    }

    suspend fun clearAllVisitedPoints(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[VISITED_POINTS_KEY] = emptySet()
        }
    }

}
