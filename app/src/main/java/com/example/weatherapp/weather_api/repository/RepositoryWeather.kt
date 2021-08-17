package com.example.weatherapp.weather_api.repository

import com.example.weatherapp.location_weather.model.LocationWeather
import com.example.weatherapp.weather_api.util.Constants.Companion.KEY_WEATHER_API
import com.example.weatherapp.weather_api.api.RetrofitWeather
import com.example.weatherapp.weather_api.model.Weather

class RepositoryWeather {
    suspend fun getCurrentWeather(locationWeather: LocationWeather): Weather{
        return RetrofitWeather.API_WEATHER.getCurrentWeather(KEY_WEATHER_API, "${locationWeather.latLocation},${locationWeather.longLocation}", "3", "yes", "yes")
    }
}