package com.lucaskempe.cropy.services.maps.models

import com.google.gson.annotations.SerializedName


data class StartLocation (

  @SerializedName("lat" ) var lat : Double = 0.0,
  @SerializedName("lng" ) var lng : Double = 0.0

)