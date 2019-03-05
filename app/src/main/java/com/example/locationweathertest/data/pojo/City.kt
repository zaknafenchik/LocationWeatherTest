package com.example.locationweathertest.data.pojo


data class City(
    val name: String,
    var distance: Double,
    val location: Location,
    var weather: Weather? = null
)