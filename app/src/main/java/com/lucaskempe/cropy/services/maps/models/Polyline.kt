package com.lucaskempe.cropy.services.maps.models

import com.google.gson.annotations.SerializedName


data class Polyline (

  @SerializedName("points" ) var points : String? = null

)