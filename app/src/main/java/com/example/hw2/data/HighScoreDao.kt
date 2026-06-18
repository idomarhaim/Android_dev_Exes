package com.example.hw2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HighScoreDao {

    /** Top ten scores since the app was installed, highest first. */
    @Query("SELECT * FROM high_scores ORDER BY score DESC, timestamp DESC LIMIT 10")
    fun topTen(): Flow<List<HighScore>>

    @Insert
    suspend fun insert(score: HighScore): Long

    @Query("DELETE FROM high_scores")
    suspend fun clear()
}
