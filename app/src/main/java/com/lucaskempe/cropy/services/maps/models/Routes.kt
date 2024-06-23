package com.lucaskempe.cropy.services.maps.models

import com.google.gson.annotations.SerializedName
import com.lucaskempe.cropy.services.maps.models.Bounds
import com.lucaskempe.cropy.services.maps.models.Legs
import com.lucaskempe.cropy.services.maps.models.OverviewPolyline


data class Routes (

    @SerializedName("bounds"            ) var bounds           : Bounds?           = Bounds(),
    @SerializedName("copyrights"        ) var copyrights       : String?           = null,
    @SerializedName("legs"              ) var legs             : ArrayList<Legs>   = arrayListOf(),
    @SerializedName("overview_polyline" ) var overviewPolyline : OverviewPolyline? = OverviewPolyline(),
    @SerializedName("summary"           ) var summary          : String?           = null,
    @SerializedName("warnings"          ) var warnings         : ArrayList<String> = arrayListOf(),
    @SerializedName("waypoint_order"    ) var waypointOrder    : ArrayList<String> = arrayListOf()

)