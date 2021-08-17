package com.example.weatherapp.location_weather.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.location_weather.model.LocationWeather

@Dao
interface LocationWeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLocationWeather(locationWeather: LocationWeather)

    @Query("SELECT * FROM tblLocationWeatherDatabase ORDER BY id ASC")
    fun readAlLLocationWeather(): LiveData< List<LocationWeather> >

    @Update
    suspend fun updateLocationWeather(locationWeather: LocationWeather)

    @Delete
    suspend fun deleteLocationWeather(locationWeather: LocationWeather)

    @Query("DELETE FROM tblLocationWeatherDatabase")
    suspend fun deleteAllLocationWeather()
}