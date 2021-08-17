package com.example.weatherapp.show_home_weather

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.weather_api.model.Hour
import kotlinx.android.synthetic.main.item_weather_hour.view.*

class WeatherHourAdapter : RecyclerView.Adapter<WeatherHourAdapter.MyViewHolder>() {
    private var weatherHourList: ArrayList<Hour> = ArrayList()
    private var indexItemSetBackground = 0
    private var settingTemperature = "°C"


    fun setData(weatherHourList: ArrayList<Hour>, tmp: String){
        this.weatherHourList.clear()
        this.weatherHourList.addAll(weatherHourList)
        settingTemperature=tmp
        notifyDataSetChanged()
    }
    fun setIndexItemSetBackground(index: Int){
        indexItemSetBackground = index
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weather_hour, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var weatherHour = weatherHourList[position]
        val result= weatherHour.time.split(" ").map { it.trim() }
        holder.itemView.tv_time.text=result[result.size-1]
        Glide.with(holder.itemView.context).load("https:${weatherHour.condition.icon}").into(holder.itemView.img_icon)
        if(settingTemperature=="°C"){
            holder.itemView.tv_temperature.text = weatherHour.temp_c.toString()
        }else{
            holder.itemView.tv_temperature.text = weatherHour.temp_f.toString()
        }

        if(position==indexItemSetBackground){
            holder.itemView.cv_item_weather_hour.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white_back))
        }else{
            holder.itemView.cv_item_weather_hour.setCardBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun getItemCount(): Int {
        return weatherHourList.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}
}