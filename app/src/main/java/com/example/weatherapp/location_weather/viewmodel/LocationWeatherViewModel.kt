package com.example.weatherapp.location_weather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.location_weather.database.LocationWeatherDatabase
import com.example.weatherapp.location_weather.model.LocationWeather
import com.example.weatherapp.location_weather.repository.LocationWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationWeatherViewModel(application: Application): AndroidViewModel(application) {
    val readAllLocationWeather:  LiveData< List<LocationWeather> >
    private val repository: LocationWeatherRepository

    init {
        val locationWeatherDao = LocationWeatherDatabase.getDatabase(application).locationWeatherDao()
        repository= LocationWeatherRepository(locationWeatherDao)
        readAllLocationWeather=repository.readAllLocationWeather
    }

    fun addLocationWeather(locationWeather: LocationWeather){
        viewModelScope.launch (Dispatchers.IO){
            repository.addLocationWeather(locationWeather)
        }
    }

    fun updateLocationWeather(locationWeather: LocationWeather){
        viewModelScope.launch (Dispatchers.IO){
            Log.d("nvhieu", "updateLocationWeather call call call call call call call call call call................. ")
            repository.updateLocationWeather(locationWeather)
        }
//        repository.updateLocationWeather(locationWeather)
    }

    fun deleteLocationWeather(locationWeather: LocationWeather){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteLocationWeather(locationWeather)
        }
    }
    fun deleteAllLocationWeather(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllLocationWeather()
        }
    }
}