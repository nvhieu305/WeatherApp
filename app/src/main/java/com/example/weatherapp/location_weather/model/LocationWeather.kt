package com.example.weatherapp.location_weather.model

import android.os.Parcelable
import androidx.room.*
import com.example.weatherapp.weather_api.model.Weather
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "tblLocationWeatherDatabase")
class LocationWeather: Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "nameLocation")
    var nameLocation: String?=null

    @ColumnInfo(name = "latLocation")
    var latLocation: Double?=null

    @ColumnInfo(name = "longLocation")
    var longLocation: Double?=null

    @ColumnInfo(name = "weatherLocation")
    var weather: Weather?=null

    @ColumnInfo(name = "timeUpdateWeatherLocation")
    var timeUpdateWeather: String? = null
}