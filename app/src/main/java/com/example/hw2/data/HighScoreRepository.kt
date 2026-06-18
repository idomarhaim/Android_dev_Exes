package com.example.hw2.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

/**
 * Thin wrapper over [HighScoreDao] so callers don't touch Room directly.
 */
class HighScoreRepository(private val dao: HighScoreDao) {

    val topTen: Flow<List<HighScore>> = dao.topTen()

    suspend fun add(score: HighScore): Long = dao.insert(score)

    companion object {
        fun from(context: Context): HighScoreRepository =
            HighScoreRepository(AppDatabase.get(context).highScoreDao())
    }
}
