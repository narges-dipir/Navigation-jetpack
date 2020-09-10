package com.narcis.shoes.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ShoeBreed::class), version = 1)
abstract class ShoeDatabase: RoomDatabase() {
    abstract fun shoeDao(): ShoeDao

    companion object {
        @Volatile private var instance: ShoeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ShoeDatabase::class.java,
            "shoedatabase"
        ).build()
    }
}