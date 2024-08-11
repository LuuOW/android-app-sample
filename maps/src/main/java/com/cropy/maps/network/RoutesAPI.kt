package com.cropy.maps.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RoutesAPI {

    @Headers(
        "Content-Type: application/json",
        //"X-Goog-Api-Key: ${BuildConfig.MAPS_API_KEY}",
        "X-Goog-FieldMask: routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline,routes.legs.*,routes.legs.travelAdvisory.speedReadingIntervals,routes.legs.polyline.encodedPolyline,routes.legs.steps.*,routes.travelAdvisory.speedReadingIntervals"
    )
    @POST("directions/v2:computeRoutes")
    suspend fun fetchDirections(@Body routeRequest: com.cropy.maps.models.RouteRequest): Response<com.cropy.maps.models.Route>

}