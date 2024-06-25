package com.lucaskempe.cropy.services.maps

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.lucaskempe.cropy.services.maps.models.distance.DistanceModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DistanceAPI {

    @GET("/maps/api/distancematrix/json")
    fun getDistance(
        @Query("destinations") destination: String,
        @Query("origins") origin: String,
        @Query("key") key: String,
        @Query("units") units: String,
        ): Call<DistanceModel>


}