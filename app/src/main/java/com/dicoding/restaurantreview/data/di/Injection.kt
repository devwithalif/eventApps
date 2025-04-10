package com.dicoding.restaurantreview.data.di

import android.content.Context
import com.dicoding.restaurantreview.data.local.room.EventDatabase
import com.dicoding.restaurantreview.data.remote.response.EventRepository
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import com.dicoding.restaurantreview.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(dao, apiService, appExecutors)
    }
}