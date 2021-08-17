package com.example.weatherapp.weather_api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.weather_api.repository.RepositoryWeather

class CurrentWeatherViewModelFactory(private val repositoryWeather: RepositoryWeather) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(repositoryWeather) as T
    }
}