package com.esteban.turismoar.data.repository

import android.content.Context
import com.esteban.turismoar.data.local.UserPreferences
import com.esteban.turismoar.domain.repository.TourRepository
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
