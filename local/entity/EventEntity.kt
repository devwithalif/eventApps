package com.dicoding.restaurantreview.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteEvent")
class EventEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "mediaCover")
    val mediaCover: String? = null,

    @field:ColumnInfo(name = "favorite")
    var favorite: Boolean = false
)