package com.dicoding.restaurantreview.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.restaurantreview.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): EventDatabase {
            if (INSTANCE == null) {
                synchronized(EventDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        EventDatabase::class.java, "event_database")
                        .build()
                }
            }
            return INSTANCE as EventDatabase
        }
    }
}
