package com.example.weatherapp.location_weather.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.location_weather.model.LocationWeather
import com.example.weatherapp.location_weather.interfaces.EventLocationWeatherAdapter
import kotlinx.android.synthetic.main.activity_get_location_weather.view.*
import kotlinx.android.synthetic.main.item_location_weather.view.*

class LocationWeatherAdapter: RecyclerView.Adapter<LocationWeatherAdapter.MyViewHolder>() {
    private val TAG="nvhieu"
    private var locationWeatherList : ArrayList<LocationWeather> = ArrayList()
    private var eventLocationWeatherAdapter: EventLocationWeatherAdapter?=null

    fun setDataLocationWeatherList(locationWeatherList: ArrayList<LocationWeather>){
        this.locationWeatherList.clear()
        this.locationWeatherList.addAll(locationWeatherList)
        notifyDataSetChanged()
    }

    fun setCallBack(eventLocationWeatherAdapter: EventLocationWeatherAdapter){
        this.eventLocationWeatherAdapter = eventLocationWeatherAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_location_weather, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var locationWeather = locationWeatherList[position]

        if(eventLocationWeatherAdapter!!.indexItem == position){
//            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.tvTitle.setTypeface(null, Typeface.BOLD_ITALIC)
            holder.itemView.img_icon_location.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.blue))
        }else {
//            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            holder.itemView.img_icon_location.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.gray))
            holder.tvTitle.setTypeface(null, Typeface.NORMAL)
        }

        holder.tvTitle.text = locationWeather.nameLocation

        holder.itemView.setOnClickListener{
            if(eventLocationWeatherAdapter!=null){
                eventLocationWeatherAdapter!!.indexItem = position
                eventLocationWeatherAdapter!!.itemLocationWeatherClick(locationWeather)
            }
        }

        holder.itemView.setOnLongClickListener {
            if(eventLocationWeatherAdapter!=null){
                eventLocationWeatherAdapter!!.itemLocationWeatherLongClick(locationWeather)
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return locationWeatherList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvTitle: TextView = itemView.findViewById(R.id.tv_location_weather)
    }
}