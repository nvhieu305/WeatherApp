package com.example.weatherapp

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.weatherapp.show_home_weather.HomeWeatherFragment

class Viewpager2HomeFragmentAdapter( activity: AppCompatActivity): FragmentStateAdapter(activity) {
    private val TAG = "nvhieu"
    private var fragments: ArrayList<HomeWeatherFragment> = ArrayList()

    private var mediaId = fragments.map { it.hashCode().toLong() }

    fun setData(fragments: ArrayList<HomeWeatherFragment>){
        this.fragments.clear()
        this.fragments.addAll(fragments)
        for(i in fragments){
            Log.d(TAG, "Viewpager2HomeFragmentAdapter setData: ${i.locationWeather.nameLocation} $i")
        }
        mediaId = fragments.map { it.hashCode().toLong() }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun getItemId(position: Int): Long = mediaId[position]

    override fun containsItem(itemId: Long): Boolean = mediaId.contains(itemId)

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}