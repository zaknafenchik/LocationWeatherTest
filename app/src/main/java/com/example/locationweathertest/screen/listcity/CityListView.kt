package com.example.locationweathertest.screen.listcity

import com.arellomobile.mvp.MvpView
import com.example.locationweathertest.data.pojo.City

interface CityListView : MvpView {
    fun showCities(it: List<City>)
    fun showWeather(city: City)
    fun showProgress(show: Boolean)
}