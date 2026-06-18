package com.example.hw2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One persisted high score: the combined score plus its breakdown and the
 * device location where the run ended (shown on the map fragment).
 */
@Entity(tableName = "high_scores")
data class HighScore(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int,
    val distance: Int,
    val coins: Int,
    val latitude: Double,
    val longitude: Double,
    val hasLocation: Boolean,
    val timestamp: Long,
)
