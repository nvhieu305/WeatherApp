package com.example.weatherapp.weather_api.api

import com.example.weatherapp.weather_api.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.weatherapi.com/v1/current.json?key=8494b1187dc84cc9a26231346210208&q=21,105&aqi=yes
//http://api.weatherapi.com/v1/forecast.json?key=8494b1187dc84cc9a26231346210208&q=22,105&days=7&aqi=yes&alerts=yes

interface ApiWeather {
    @GET("/v1/forecast.json?")
    suspend fun getCurrentWeather(@Query("key") key: String, @Query("q") q: String, @Query("days") days: String, @Query("aqi") api: String, @Query("alerts") alerts: String): Weather
}