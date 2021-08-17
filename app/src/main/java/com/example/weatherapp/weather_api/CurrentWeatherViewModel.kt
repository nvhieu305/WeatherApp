package com.example.weatherapp.weather_api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.location_weather.model.LocationWeather
import com.example.weatherapp.weather_api.model.Weather
import com.example.weatherapp.weather_api.repository.RepositoryWeather
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class CurrentWeatherViewModel (private var repositoryWeather: RepositoryWeather): ViewModel(){

    val myCurrentWeatherRepository: MutableLiveData<Weather> = MutableLiveData()

    fun getCurrentWeather(locationWeather: LocationWeather) {
        viewModelScope.launch {
            try{
                val response = repositoryWeather.getCurrentWeather(locationWeather)
                myCurrentWeatherRepository.value = response
            }catch (e: CancellationException){
                throw e
            }catch (e: Exception){
                // log error
            }
        }
    }
}