package com.example.weatherapp.weather_api.api

import com.example.weatherapp.weather_api.util.Constants.Companion.BASE_URL_WEATHER_API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitWeather {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val API_WEATHER: ApiWeather by lazy {
        retrofit.create(ApiWeather::class.java)
    }
}