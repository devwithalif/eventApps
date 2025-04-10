package com.dicoding.restaurantreview.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.restaurantreview.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM favoriteEvent")
    fun getNews(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: List<EventEntity>)

    @Update
    fun updateEvent(Event: EventEntity)

    @Query("DELETE FROM favoriteEvent")
    fun deleteAll()

}