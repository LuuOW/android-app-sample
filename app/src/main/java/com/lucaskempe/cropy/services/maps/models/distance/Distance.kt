package com.lucaskempe.cropy.services.maps.models.distance

import com.google.gson.annotations.SerializedName


data class Distance (

  @SerializedName("text"  ) var text  : String? = null,
  @SerializedName("value" ) var value : Int?    = null

)