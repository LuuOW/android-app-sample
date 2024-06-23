package com.lucaskempe.cropy.services.maps

import com.lucaskempe.cropy.services.maps.models.Directions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DirectionsAPI {

    @GET("/maps/api/directions/json")
    fun getDirectionsTo(@Query("origin") origin: String,  @Query("destination") destination: String, @Query("key") key: String): Call<Directions>

}