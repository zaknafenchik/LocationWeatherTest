package com.example.locationweathertest.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCityApi {

    @GET("GetNearbyCities")
    fun loadCity(
        @Query("Latitude") lat: Double,
        @Query("Longitude") lon: Double,
        @Query("radius") radius: Int,
        @Query("limit") limit: Int = 30
    ): Single<List<List<String>>>
}
