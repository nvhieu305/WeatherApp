package com.example.weatherapp.show_home_weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.location_weather.model.LocationWeather
import com.example.weatherapp.location_weather.viewmodel.LocationWeatherViewModel
import com.example.weatherapp.weather_api.CurrentWeatherViewModel
import com.example.weatherapp.weather_api.CurrentWeatherViewModelFactory
import com.example.weatherapp.weather_api.model.Hour
import com.example.weatherapp.weather_api.model.Weather
import com.example.weatherapp.weather_api.repository.RepositoryWeather
import kotlinx.android.synthetic.main.fragment_home_weather.*
import kotlinx.android.synthetic.main.fragment_home_weather.view.*
import java.text.SimpleDateFormat
import java.util.*


class HomeWeatherFragment : Fragment() {
    private var TAG = "nvhieu"

    var locationWeather: LocationWeather = LocationWeather()
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel
    private var weather: Weather?=null
    private var weatherHourAdapter = WeatherHourAdapter()
    private lateinit var mLocationWeatherViewModel:LocationWeatherViewModel
    private var settingTemperature = "°C"

    fun setData(locationWeather: LocationWeather){
        this.locationWeather = locationWeather
        if(locationWeather.weather!=null){
            weather=locationWeather.weather
        }
        Log.d(TAG, "HomeWeatherFragment setData: ${locationWeather.nameLocation} ${locationWeather.latLocation} ${locationWeather.longLocation} ${locationWeather.weather?.current?.condition?.text}")
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home_weather, container, false)

        getSettingTemperature()

        view.recycler_view_weather_hour.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.recycler_view_weather_hour.adapter=weatherHourAdapter

        mLocationWeatherViewModel = ViewModelProvider(this).get(LocationWeatherViewModel::class.java)

        getCurrentWeather()

        view.img_btn_update.setOnClickListener {
            getCurrentWeather()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(weather!=null){
            setView()
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "HomeWeatherFragment onResume: ${locationWeather.nameLocation} ${locationWeather.latLocation} ${locationWeather.longLocation}")

        if(weather!=null){
            setView()
        }
    }


    @SuppressLint("FragmentLiveDataObserve")
    private fun getCurrentWeather(){
        if(this.locationWeather!=null){
            val repository = RepositoryWeather()
            val currentWeatherViewModelFactory = CurrentWeatherViewModelFactory(repository)
            currentWeatherViewModel = ViewModelProvider(this, currentWeatherViewModelFactory).get(CurrentWeatherViewModel::class.java)
            currentWeatherViewModel.getCurrentWeather(locationWeather)
            currentWeatherViewModel.myCurrentWeatherRepository.observe(this, androidx.lifecycle.Observer {
                if(it!=null){
                    weather=it
                    locationWeather.weather = weather
                    locationWeather.timeUpdateWeather = getTimeUpdateWeather()
                    mLocationWeatherViewModel.updateLocationWeather(locationWeather)
                    setView()
                    currentWeatherViewModel.myCurrentWeatherRepository.removeObservers(this)

                }
            })

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){

        getSettingTemperature()

        Log.d(TAG, "HomeWeatherFragment setView: ${locationWeather.nameLocation} ${locationWeather.latLocation} ${locationWeather.longLocation}  ${weather?.location?.name}")
        setHomeWeatherBackground()
        setIconWeather()

        tv_time_update.text = "Update: ${locationWeather.timeUpdateWeather}"

        tv_temperature_unit.text=settingTemperature
        tv_location.text = locationWeather.nameLocation
        tv_status.text = weather?.current?.condition?.text.toString()

        if(settingTemperature=="°C"){
            tv_temperature_unit.text = settingTemperature
            tv_min_temperature.text= weather?.forecast?.forecastday?.get(0)?.day?.mintemp_c.toString() + settingTemperature
            tv_max_temperature.text= weather?.forecast?.forecastday?.get(0)?.day?.maxtemp_c.toString() + settingTemperature
            tv_temperature.text = weather?.current?.temp_c?.toInt().toString()
            tv_details_temperature.text=weather?.current?.feelslike_c.toString()  + settingTemperature
        }else{
            tv_temperature_unit.text = settingTemperature
            tv_min_temperature.text= weather?.forecast?.forecastday?.get(0)?.day?.mintemp_f.toString() + settingTemperature
            tv_max_temperature.text= weather?.forecast?.forecastday?.get(0)?.day?.maxtemp_f.toString() + settingTemperature
            tv_temperature.text = weather?.current?.temp_f?.toInt().toString()
            tv_details_temperature.text=weather?.current?.feelslike_f.toString()  + settingTemperature
        }


        //DETAILS
        tv_details_humidity.text=weather?.current?.humidity.toString() +"%"
        tv_details_uv.text=weather?.current?.uv.toString()
        tv_details_visibility.text=weather?.current?.vis_km.toString()+"km"

        //AIR
        tv_air_co.text="CO: "+ String.format("%.2f", weather?.current?.air_quality?.co)
        tv_air_o3.text="O3: "+ String.format("%.2f", weather?.current?.air_quality?.o3)
        tv_air_no2.text="NO2: "+ String.format("%.2f", weather?.current?.air_quality?.no2)
        tv_air_so2.text="SO2: "+ String.format("%.2f", weather?.current?.air_quality?.so2)
        tv_air_pm2_5.text="Pm2.5: "+ String.format("%.2f", weather?.current?.air_quality?.pm2_5)
        tv_air_pm10.text="Pm10: "+ String.format("%.2f", weather?.current?.air_quality?.pm10)
        tv_air_uk_defra_index_number.text=weather?.current?.air_quality?.gb_defra_index.toString()
        when (weather?.current?.air_quality?.gb_defra_index){
            1,2,3->{
                tv_air_uk_defra_text.text="Enjoy your usual outdoor activities."
            }
            4,5,6->{
                tv_air_uk_defra_text.text="Enjoy your usual outdoor activities."
            }
            7,8,9->{
                tv_air_uk_defra_text.text="Anyone experiencing discomfort such as sore eyes, cough or sore throat should consider reducing activity, particularly outdoors."
            }
            10->{
                tv_air_uk_defra_text.text="Reduce physical exertion, particularly outdoors, especially if you experience symptoms such as cough or sore throat."
            }
        }
        var index:Float = weather?.current?.air_quality?.gb_defra_index!!.toFloat()
        index *= 30
        img_icon_air_show_uk_defra_index.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            val r: Resources = context?.resources!!
            val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                index,
                r.displayMetrics
            ).toInt()
            setMargins(px,0,0,0)
        }

        //WIND and PRESSURE
        tv_wind.text=weather?.current?.wind_kph.toString() + " km/h"
        tv_wind_direction.text="wind direction: "+weather?.current?.wind_dir.toString()
        tv_pressure.text=weather?.current?.pressure_mb.toString() + " mbar"

        //CHANCE OF RAIN
        setChanceOfRain()

        getDateTime()

        weatherHourAdapter.setData(weather?.forecast?.forecastday?.get(0)?.hour as ArrayList<Hour>, settingTemperature)
    }

    private fun setPx(index: Float): Int {
        val r: Resources = context?.resources!!
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            index,
            r.displayMetrics
        ).toInt()
    }

    @SuppressLint("SetTextI18n")
    private fun setChanceOfRain(){
        val simpleDateFormatHour = SimpleDateFormat("HH")
        var index = simpleDateFormatHour.format(Date()).toInt()

        var indexChanceRain2:Float
        var indexChanceRain3:Float
        var indexChanceRain4:Float

        tv_chance_rain_collum_1.text="${index}:00"
        var indexChanceRain1 = weather?.forecast?.forecastday?.get(0)!!.hour[index].chance_of_rain.toFloat()
        index++

        if(index>23){
            tv_chance_rain_collum_2.text= "${index%24}:00"
            indexChanceRain2 = weather?.forecast?.forecastday?.get(1)!!.hour[(index)%24].chance_of_rain.toFloat()
        }else{
            tv_chance_rain_collum_2.text= "$index:00"
            indexChanceRain2 = weather?.forecast?.forecastday?.get(0)!!.hour[index].chance_of_rain.toFloat()
        }

        index++
        if(index>23){
            tv_chance_rain_collum_3.text= "${index%24}:00"
            indexChanceRain3 = weather?.forecast?.forecastday?.get(1)!!.hour[(index)%24].chance_of_rain.toFloat()
        }else{
            tv_chance_rain_collum_3.text= "$index:00"
            indexChanceRain3 = weather?.forecast?.forecastday?.get(0)!!.hour[index].chance_of_rain.toFloat()
        }

        index++
        if(index>23){
            tv_chance_rain_collum_4.text= "${index%24}:00"
            indexChanceRain4 = weather?.forecast?.forecastday?.get(1)!!.hour[(index)%24].chance_of_rain.toFloat()
        }else{
            tv_chance_rain_collum_4.text= "$index:00"
            indexChanceRain4 = weather?.forecast?.forecastday?.get(0)!!.hour[index].chance_of_rain.toFloat()
        }

        tv_chance_rain_collum_1_index.text= indexChanceRain1.toInt().toString()+ "%"
        tv_chance_rain_collum_2_index.text= indexChanceRain2.toInt().toString()+ "%"
        tv_chance_rain_collum_3_index.text= indexChanceRain3.toInt().toString()+ "%"
        tv_chance_rain_collum_4_index.text= indexChanceRain4.toInt().toString()+ "%"

        view_chance_rain_collum_1.layoutParams.height=setPx(indexChanceRain1)
        view_chance_rain_collum_2.layoutParams.height=setPx(indexChanceRain2)
        view_chance_rain_collum_3.layoutParams.height=setPx(indexChanceRain3)
        view_chance_rain_collum_4.layoutParams.height=setPx(indexChanceRain4)
    }

    private fun setHomeWeatherBackground(){
        if(weather?.current?.is_day==1){
            Glide.with(requireContext()).load(R.drawable.bg_test).into(img_view_background)
        }else{
            Glide.with(requireContext()).load(R.drawable.bg_two).into(img_view_background)
        }
    }

    private fun setIconWeather(){
        if(weather?.current?.is_day==1){
            when(weather?.current?.condition?.code){
                1000->{
                    Glide.with(requireContext()).load(R.drawable.day_1000).into(img_icon_weather_3d)
                }
                1003->{
                    Glide.with(requireContext()).load(R.drawable.day_1003).into(img_icon_weather_3d)
                }
                1006->{
                    Glide.with(requireContext()).load(R.drawable.day_1006).into(img_icon_weather_3d)
                }
                1009->{
                    Glide.with(requireContext()).load(R.drawable.day_1009).into(img_icon_weather_3d)
                }
                1030,1135,1147->{
                    Glide.with(requireContext()).load(R.drawable.day_1030_1135_1147).into(img_icon_weather_3d)
                }
                1063,1069,1180,1186,1192,1240,1243,1246,1249,1252->{
                    Glide.with(requireContext()).load(R.drawable.day_1063_1069_1180_1186_1192_1240_1243_1246_1249_1252).into(img_icon_weather_3d)
                }
                1066,1210,1216,1222,1255,1261,1264 ->{
                    Glide.with(requireContext()).load(R.drawable.day_1066_1210_1216_1222_1255_1261_1264_).into(img_icon_weather_3d)
                }
                1072,1114,1117,1168,1171,1198,1201,1213,1219,1225,1237->{
                    Glide.with(requireContext()).load(R.drawable.day_1072_1114_1117_1168_1171_1198_1201_1213_1219_1225_1237).into(img_icon_weather_3d)
                }
                1087,1273,1279->{
                    Glide.with(requireContext()).load(R.drawable.day_1087_1273_1279).into(img_icon_weather_3d)
                }
                1150,1153,1183,1189,1195,1204->{
                    Glide.with(requireContext()).load(R.drawable.day_1150_1153_1183_1189_1195_1204).into(img_icon_weather_3d)
                }
                1276,1282->{
                    Glide.with(requireContext()).load(R.drawable.day_1276_1282).into(img_icon_weather_3d)
                }
            }
        }else{
            when(weather?.current?.condition?.code){
                1000->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1000).into(img_icon_weather_3d)
                }
                1003->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1003).into(img_icon_weather_3d)
                }
                1006->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1006).into(img_icon_weather_3d)
                }
                1009->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1009).into(img_icon_weather_3d)
                }
                1030,1135,1147->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1030_1135_1147).into(img_icon_weather_3d)
                }
                1063,1069,1180,1186,1192,1240,1243,1246,1249,1252->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1063_1069_1180_1186_1192_1240_1243_1246_1249_1252).into(img_icon_weather_3d)
                }
                1066,1210,1216,1222,1255,1261,1264 ->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1066_1210_1216_1222_1255_1261_1264_).into(img_icon_weather_3d)
                }
                1072,1114,1117,1168,1171,1198,1201,1213,1219,1225,1237->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1072_1114_1117_1168_1171_1198_1201_1213_1219_1225_1237).into(img_icon_weather_3d)
                }
                1087,1273,1279->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1087_1273_1279).into(img_icon_weather_3d)
                }
                1150,1153,1183,1189,1195,1204->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1150_1153_1183_1189_1195_1204).into(img_icon_weather_3d)
                }
                1276,1282->{
                    Glide.with(requireContext()).load(R.drawable.nigth_1276_1282).into(img_icon_weather_3d)
                }
            }
        }
    }


//    "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//    "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//    "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//    "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//    "yyMMddHHmmssZ"-------------------- 010704120856-0700
//    "K:mm a, z" ----------------------- 0:08 PM, PDT
//    "h:mm a" -------------------------- 12:08 PM
//    "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(){
        val simpleDateFormat = SimpleDateFormat("EEE, dd.MM.yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        tv_date.text=currentDateAndTime


        val simpleDateFormatHour = SimpleDateFormat("HH")
        val hour = simpleDateFormatHour.format(Date())
        recycler_view_weather_hour.smoothScrollToPosition(hour.toInt())
        weatherHourAdapter.setIndexItemSetBackground(hour.toInt())
    }


    private fun getTimeUpdateWeather(): String? {
        val simpleDateFormatTime = SimpleDateFormat("HH:mm:ss")
        return simpleDateFormatTime.format(Date())
    }

    private fun getSettingTemperature(){
        val sharePreferences = requireActivity().getSharedPreferences("temperature", Context.MODE_PRIVATE)
        settingTemperature = sharePreferences.getString("STRING_KEY", "°C").toString()
    }
}