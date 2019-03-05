package com.example.locationweathertest.screen.map

import com.arellomobile.mvp.MvpView
import com.example.locationweathertest.data.pojo.City

interface MapView : MvpView {
    fun showProgress(show: Boolean)
    fun showCities(list: List<City>)
}