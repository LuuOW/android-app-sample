package com.lucaskempe.cropy.services.maps.models.distance

import com.google.gson.annotations.SerializedName
import com.lucaskempe.cropy.services.maps.models.distance.Rows


data class DistanceModel (

  @SerializedName("destination_addresses" ) var destinationAddresses : ArrayList<String> = arrayListOf(),
  @SerializedName("origin_addresses"      ) var originAddresses      : ArrayList<String> = arrayListOf(),
  @SerializedName("rows"                  ) var rows                 : ArrayList<Rows>   = arrayListOf(),
  @SerializedName("status"                ) var status               : String?           = null

)