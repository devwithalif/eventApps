package com.dicoding.restaurantreview.data.remote.retrofit

import com.dicoding.restaurantreview.data.remote.response.DetailEventResponse
import com.dicoding.restaurantreview.data.remote.response.EventResponse
import com.dicoding.restaurantreview.data.remote.response.PostReviewResponse
import com.dicoding.restaurantreview.data.remote.response.RestaurantResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvent(
            @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<DetailEventResponse>

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Any = -1,
        @Query("q") query: String
    ): Call<EventResponse>

    @FormUrlEncoded
    @Headers("Authorization: token 12345")
    @POST("review")
    fun postReview(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("review") review: String
    ): Call<PostReviewResponse>
}