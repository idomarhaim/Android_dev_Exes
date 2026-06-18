package com.example.hw2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HighScore::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun highScoreDao(): HighScoreDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hw2_high_scores.db"
                ).build().also { instance = it }
            }
    }
}
