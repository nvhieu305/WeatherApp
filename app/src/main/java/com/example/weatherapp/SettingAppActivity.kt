package com.example.weatherapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_setting_app.*

class SettingAppActivity : AppCompatActivity() {
    private var settingTemperature = "°C"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_app)



        img_btn_back.setOnClickListener {
            finish()
        }
        getSettingTemperature()
        if(settingTemperature=="°C"){
            rd_btn_c.isChecked=true
        }else{
            rd_btn_f.isChecked=true
        }

        rd_btn_c.setOnClickListener {
            saveSettingTemperature("°C")
            getSettingTemperature()
        }
        rd_btn_f.setOnClickListener {
            saveSettingTemperature("°F")
            getSettingTemperature()
        }
    }

    private fun saveSettingTemperature(string : String){
        val sharePreferences = getSharedPreferences("temperature", Context.MODE_PRIVATE)
        val editor = sharePreferences.edit()
        editor.apply{
            putString("STRING_KEY", string)
        }.apply()
    }

    private fun getSettingTemperature(){
        val sharePreferences = getSharedPreferences("temperature", Context.MODE_PRIVATE)
        settingTemperature = sharePreferences.getString("STRING_KEY", "°C").toString()
    }

}