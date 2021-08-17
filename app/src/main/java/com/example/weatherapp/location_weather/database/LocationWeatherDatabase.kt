package com.example.weatherapp.location_weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.location_weather.model.Converters
import com.example.weatherapp.location_weather.model.LocationWeather

@Database(entities = [LocationWeather::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocationWeatherDatabase: RoomDatabase() {

    abstract fun locationWeatherDao(): LocationWeatherDao

    companion object{
        @Volatile
        private var INSTANCE: LocationWeatherDatabase?=null

        fun getDatabase(context: Context): LocationWeatherDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    LocationWeatherDatabase::class.java,
                    "location_weather_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

//    companion object{
//        @Volatile
//        private var instance: LocationWeatherDatabase?=null
//        private val LOCK = Any()
//        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
//            instance?: createDatabase(context).also{
//                instance = it
//            }
//
//        }
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                LocationWeatherDatabase::class.java,
//                "weather.db"
//            ).build()
//    }
}