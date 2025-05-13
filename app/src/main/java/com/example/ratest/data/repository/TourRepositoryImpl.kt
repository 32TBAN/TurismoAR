package com.example.ratest.data.repository

import android.content.Context
import com.example.ratest.data.local.UserPreferences
import com.example.ratest.domain.repository.TourRepository
import kotlinx.coroutines.flow.first

class TourRepositoryImpl(private val context: Context) : TourRepository {
    override suspend fun getVisitedPoints(): Set<String> {
        return UserPreferences.getVisitedPoints(context).first().toSet()
    }

    override suspend fun markPointAsVisited(name: String) {
        UserPreferences.markPointAsVisited(context, name)
    }

    override suspend fun clearVisitedPoints() {
        UserPreferences.clearAllVisitedPoints(context)
    }
}
