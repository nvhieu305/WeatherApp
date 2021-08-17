package com.example.weatherapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.size
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.location_weather.GetLocationWeatherActivity
import com.example.weatherapp.location_weather.model.LocationWeather
import com.example.weatherapp.location_weather.adapter.LocationWeatherAdapter
import com.example.weatherapp.location_weather.interfaces.EventLocationWeatherAdapter
import com.example.weatherapp.location_weather.viewmodel.LocationWeatherViewModel
import com.example.weatherapp.show_home_weather.HomeWeatherFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG="nvhieu"

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var imgBtnClickDrawerLayout: ImageButton
    private lateinit var recyclerviewLocationWeather: RecyclerView
    private lateinit var imgBtnAddLocationWeather: Button
    private lateinit var viewPager2HomeWeather: ViewPager2

    private var locationWeatherList: ArrayList<LocationWeather> = ArrayList()
    private var locationWeatherAdapter: LocationWeatherAdapter = LocationWeatherAdapter()
    private var homeWeatherFragmentList: ArrayList<HomeWeatherFragment> = ArrayList()
    private lateinit var viewpager2HomeFragmentAdapter: Viewpager2HomeFragmentAdapter
    private lateinit var mLocationWeatherViewModel: LocationWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        connectUxUi()

        mLocationWeatherViewModel=ViewModelProvider(this).get(LocationWeatherViewModel::class.java)
        liveDataLocationWeather()

        // setting navigation drawerLayout:
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        imgBtnClickDrawerLayout.setOnClickListener(View.OnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        })

        navigationView()

        imgBtnAddLocationWeather.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, GetLocationWeatherActivity::class.java)
            drawerLayout.closeDrawer(Gravity.LEFT)
            startActivity(intent)
        })

        img_btn_setting.setOnClickListener{
            var intent = Intent(this, SettingAppActivity::class.java)
            drawerLayout.closeDrawer(Gravity.LEFT)
            startActivity(intent)
        }

        // call back viewpager2:
        viewPager2HomeWeather.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                eventLocationWeatherAdapter.indexItem = position
                recyclerviewLocationWeather.smoothScrollToPosition(position)
                locationWeatherAdapter.notifyDataSetChanged()
            }
        })
    }


    private fun liveDataLocationWeather(){
        var indexSizeLocationWeather = locationWeatherList.size

        mLocationWeatherViewModel.readAllLocationWeather.observe(this, Observer {
            locationWeatherList = it as ArrayList<LocationWeather>
            Log.d(TAG, "liveDataLocationWeather: ${locationWeatherList.size}")
            if(indexSizeLocationWeather != it.size){
                indexSizeLocationWeather = it.size
                homeWeatherFragmentList.clear()
                for(i in locationWeatherList){
                    val homeWeatherFragment = HomeWeatherFragment()
                    homeWeatherFragment.setData(i)
                    homeWeatherFragmentList.add(homeWeatherFragment)
                }
                locationWeatherAdapter.setDataLocationWeatherList(locationWeatherList)
                viewpager2HomeFragmentAdapter.setData(homeWeatherFragmentList)
            }

        })
    }

    private fun connectUxUi(){
        drawerLayout = findViewById(R.id.nav_drawer_layout)
        imgBtnClickDrawerLayout = findViewById(R.id.img_btn_click_nav_drawer)
        recyclerviewLocationWeather = findViewById(R.id.recycler_view)
        imgBtnAddLocationWeather = findViewById(R.id.img_btn_add_location)
        viewPager2HomeWeather = findViewById(R.id.viewpager2_layout)
    }

    // Code in NavigationView:
    private fun navigationView(){
        recyclerviewLocationWeather.adapter = locationWeatherAdapter
        recyclerviewLocationWeather.layoutManager = LinearLayoutManager(this)
        viewpager2View()
        locationWeatherAdapter.setCallBack(eventLocationWeatherAdapter)
    }

    // Viewpager2 show list Fragment HomeWeather:
    private fun viewpager2View(){
        viewpager2HomeFragmentAdapter = Viewpager2HomeFragmentAdapter( this)
        viewPager2HomeWeather.adapter = viewpager2HomeFragmentAdapter
    }


    private var eventLocationWeatherAdapter: EventLocationWeatherAdapter = object : EventLocationWeatherAdapter{
        override var indexItem: Int = 0

        override fun itemLocationWeatherLongClick(locationWeather: LocationWeather) {
            mLocationWeatherViewModel.deleteLocationWeather(locationWeather)
            locationWeatherAdapter.setDataLocationWeatherList(locationWeatherList)
        }

        override fun itemLocationWeatherClick(locationWeather: LocationWeather) {
            Log.d(TAG, "MainActivity itemLocationWeatherClick: ${locationWeather.nameLocation} $indexItem")
            drawerLayout.closeDrawer(Gravity.LEFT)
//            viewPager2HomeWeather.currentItem = indexItem
            viewPager2HomeWeather.setCurrentItem(indexItem, false)
            Log.d(TAG, "MainActivity itemLocationWeatherClick: ${viewPager2HomeWeather.currentItem}")
        }
    }


    private fun setWindowFlag(activity: Activity, bits: Int, state: Boolean) {
        val win: Window = activity.window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (state) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
    }
}