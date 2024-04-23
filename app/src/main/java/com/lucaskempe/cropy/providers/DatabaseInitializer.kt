package com.lucaskempe.cropy.providers

import android.content.Context
import androidx.room.Room

class DatabaseInitializer private constructor(context: Context) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "image_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    val imageDao: ImageDao by lazy { database.imageDao() }

    companion object {
        @Volatile
        private var instance: DatabaseInitializer? = null

        fun getInstance(context: Context): DatabaseInitializer {
            if (instance == null) {
                synchronized(DatabaseInitializer::class.java) {
                    if (instance == null) {
                        instance = DatabaseInitializer(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }
}
