package com.lucaskempe.cropy.services.maps.models

import com.google.gson.annotations.SerializedName


data class Directions (

  @SerializedName("geocoded_waypoints" ) var geocodedWaypoints : ArrayList<GeocodedWaypoints> = arrayListOf(),
  @SerializedName("routes"             ) var routes            : ArrayList<Routes>            = arrayListOf(),
  @SerializedName("status"             ) var status            : String?                      = null

)