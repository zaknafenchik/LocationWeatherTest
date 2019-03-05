package com.example.locationweathertest.data.repo

import com.example.locationweathertest.LocationProvider
import com.example.locationweathertest.data.api.GeoCityApi
import com.example.locationweathertest.data.api.WeatherApi
import com.example.locationweathertest.data.pojo.City
import com.example.locationweathertest.data.pojo.Location
import com.example.locationweathertest.data.pojo.Weather
import io.reactivex.Single

class RepoImpl(private val geoCityApi: GeoCityApi, private val weatherApi: WeatherApi) : Repo {

    private var radius = 500

    override fun loadNearbyCities(location: Location): Single<List<City>> {
        return geoCityApi.loadCity(location.lat, location.lng, radius)
            .map {
                val listCity = arrayListOf<City>()
                for (list in it) {
                    listCity.add(City(list[1], list[7].toDouble(), Location(list[8].toDouble(), list[10].toDouble())))
                }
                listCity
            }
    }

    override fun loadWeather(city: City): Single<City> {
        return weatherApi.loadWeather(city.location.lat, city.location.lng)
            .map {
                city.weather = Weather(it.main.temp)
                city
            }
    }

    override fun setRadius(radius: Int) {
        this.radius = radius
        LocationProvider.searchRadiusChanged()
    }
}