package com.example.weatherapp.location_weather.interfaces

import com.example.weatherapp.location_weather.model.LocationWeather

interface EventLocationWeatherAdapter {
    var indexItem: Int
    fun itemLocationWeatherLongClick(locationWeather: LocationWeather)
    fun itemLocationWeatherClick(locationWeather: LocationWeather)
}