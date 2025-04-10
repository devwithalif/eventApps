package com.dicoding.restaurantreview.data.remote.response

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.restaurantreview.data.local.entity.EventEntity
import com.dicoding.restaurantreview.data.local.room.EventDao
import com.dicoding.restaurantreview.data.remote.ResultApi
import com.dicoding.restaurantreview.data.remote.retrofit.ApiService
import com.dicoding.restaurantreview.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository(
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors,
    private val apiService: ApiService
) {
    private val result = MediatorLiveData<ResultApi<List<EventEntity>>>()

    fun getHeadlineEvent(): LiveData<ResultApi<List<EventEntity>>> {
        result.value = ResultApi.Loading
        val client = apiService.getEvent(1)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val eventResponse = response.body()?.listEvents
                    val EventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        eventResponse?.forEach { event ->
                            val event = EventEntity(
                                name = event.name!!,
                                mediaCover = event.mediaCover!!,
                                id = event.id!!
                            )
                            EventList.add(event)
                        }
                        eventDao.deleteAll()
                        eventDao.insertEvent(EventList)
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = ResultApi.Error(t.message.toString())
            }
        })
        val localData = eventDao.getNews()
        result.addSource(localData) { newData: List<EventEntity> ->
            result.value = ResultApi.Success(newData)
        }
        return result
    }

    fun setBookmarkedEvent(event: EventEntity) {
        appExecutors.diskIO.execute {
            eventDao.insertEvent(listOf(event))
        }
    }

    companion object{
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            eventDao: EventDao,
            apiService: ApiService,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventDao, appExecutors, apiService)
            }.also { instance = it }
    }
}