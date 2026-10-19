package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.LessonProgressEntity
import com.example.data.model.PronunciationPracticeEntity
import com.example.data.model.SavedWordEntity
import com.example.data.model.UserStatsEntity

@Database(
    entities = [
        LessonProgressEntity::class,
        PronunciationPracticeEntity::class,
        SavedWordEntity::class,
        UserStatsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lingualearn_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
