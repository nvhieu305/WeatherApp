package com.example.weatherapp.location_weather.model

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherapp.weather_api.model.Weather
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun toWeather(weather: String?): Weather?{
        return Gson().fromJson<Weather>(weather, Weather::class.java)
    }
    @TypeConverter
    fun fromWeather(weather: Weather?): String?{
        return Gson().toJson(weather)
    }

}