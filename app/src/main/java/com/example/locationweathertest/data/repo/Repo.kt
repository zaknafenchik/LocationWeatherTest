package com.example.locationweathertest.data.repo

import com.example.locationweathertest.data.pojo.City
import com.example.locationweathertest.data.pojo.Location
import io.reactivex.Single

interface Repo {

    fun loadNearbyCities(location: Location): Single<List<City>>

    fun loadWeather(city: City):Single<City>
    fun setRadius(radius: Int)
}