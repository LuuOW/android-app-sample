package com.lucaskempe.cropy.services.maps.models.distance

import com.google.gson.annotations.SerializedName
import com.lucaskempe.cropy.services.maps.models.distance.Elements


data class Rows (

  @SerializedName("elements" ) var elements : ArrayList<Elements> = arrayListOf()

)