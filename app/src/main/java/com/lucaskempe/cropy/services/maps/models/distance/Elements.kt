package com.lucaskempe.cropy.services.maps.models.distance

import com.google.gson.annotations.SerializedName
import com.lucaskempe.cropy.services.maps.models.distance.Distance
import com.lucaskempe.cropy.services.maps.models.distance.Duration


data class Elements (

    @SerializedName("distance" ) var distance : Distance? = Distance(),
    @SerializedName("duration" ) var duration : Duration? = Duration(),
    @SerializedName("status"   ) var status   : String?   = null

)