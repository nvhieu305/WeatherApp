package com.example.weatherapp.location_weather.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherapp.location_weather.database.LocationWeatherDao
import com.example.weatherapp.location_weather.model.LocationWeather

class LocationWeatherRepository (private val locationWeatherDao: LocationWeatherDao){
    val readAllLocationWeather:  LiveData< List<LocationWeather> > = locationWeatherDao.readAlLLocationWeather()

    suspend fun addLocationWeather(locationWeather: LocationWeather){
        locationWeatherDao.addLocationWeather(locationWeather)
    }

    suspend fun updateLocationWeather(locationWeather: LocationWeather){
        locationWeatherDao.updateLocationWeather(locationWeather)
    }

    suspend fun deleteLocationWeather(locationWeather: LocationWeather){
        locationWeatherDao.deleteLocationWeather(locationWeather)
    }

    suspend fun deleteAllLocationWeather(){
        locationWeatherDao.deleteAllLocationWeather()
    }
}