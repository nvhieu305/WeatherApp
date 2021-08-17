package com.example.weatherapp.location_weather

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.location_weather.model.LocationWeather
import com.example.weatherapp.location_weather.viewmodel.LocationWeatherViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_get_location_weather.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class GetLocationWeatherActivity : AppCompatActivity() {
    private val TAG = "nvhieu"
    private val LOCATION_REQEST_CODE = 305
    private var permissionLocationCheck = false
    
    private lateinit var currentLocationWeather: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnSearch: ImageButton
    private lateinit var edtLocation: EditText

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var locationWeather: LocationWeather = LocationWeather()
    private lateinit var mLocationWeatherViewModel: LocationWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_location_weather)

        connectUxUi()

        mLocationWeatherViewModel = ViewModelProvider(this).get(LocationWeatherViewModel::class.java)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        locationRequest = LocationRequest.create()
        locationRequest.interval=4000
        locationRequest.fastestInterval=2000
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY

        currentLocationWeather.setOnClickListener {
            checkPermissionLocation()
            if(permissionLocationCheck){
                checkSettingAndLocationUpdate()
            }
        }

        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        btnSearch.setOnClickListener(View.OnClickListener {
            getLocationByAddress()
        })

        btn_location_ha_noi.setOnClickListener{
            locationWeather.nameLocation="Ha Noi"
            locationWeather.latLocation=21.030653
            locationWeather.longLocation=105.847130
            saveLocationWeather()
        }
        btn_location_ho_chi_minh.setOnClickListener{
            locationWeather.nameLocation="Ho Chi Minh"
            locationWeather.latLocation=10.762622
            locationWeather.longLocation=106.660172
            saveLocationWeather()
        }
        btn_location_Da_Nang.setOnClickListener{
            locationWeather.nameLocation="Da Nang"
            locationWeather.latLocation=16.047079
            locationWeather.longLocation=108.206230
            saveLocationWeather()
        }
        btn_location_hue.setOnClickListener{
            locationWeather.nameLocation="Hue"
            locationWeather.latLocation=16.463713
            locationWeather.longLocation=107.590866
            saveLocationWeather()
        }
        btn_location_nha_trang.setOnClickListener{
            locationWeather.nameLocation="Nha Trang"
            locationWeather.latLocation=12.243480
            locationWeather.longLocation=109.196091
            saveLocationWeather()
        }
        btn_location_vung_tau.setOnClickListener{
            locationWeather.nameLocation="Vung Tau"
            locationWeather.latLocation=10.502307
            locationWeather.longLocation=107.169205
            saveLocationWeather()
        }
        btn_location_london.setOnClickListener{
            locationWeather.nameLocation="London"
            locationWeather.latLocation=51.509865
            locationWeather.longLocation=-0.118092
            saveLocationWeather()
        }
        btn_location_japan_tokyo.setOnClickListener{
            locationWeather.nameLocation="Japan"
            locationWeather.latLocation=35.652832
            locationWeather.longLocation=139.839478
            saveLocationWeather()
        }
        btn_location_france_paris.setOnClickListener{
            locationWeather.nameLocation="Paris"
            locationWeather.latLocation=48.864716
            locationWeather.longLocation=2.349014
            saveLocationWeather()
        }

    }

    override fun onStart() {
        super.onStart()
        checkPermissionLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdate()
    }

    private fun connectUxUi() {
        currentLocationWeather = findViewById(R.id.btn_get_location)
        btnBack=findViewById(R.id.img_btn_back)
        btnSearch=findViewById(R.id.img_btn_find)
        edtLocation=findViewById(R.id.edt_location)
    }

    // Sava data:
    private fun saveLocationWeather(){
        mLocationWeatherViewModel.addLocationWeather(locationWeather)
        Log.d(TAG, "GetLocationWeatherActivity save location: ${locationWeather.nameLocation} ${locationWeather.latLocation} ${locationWeather.longLocation}")
        finish()
    }



    // LOCATION:
    private var locationCallback = object : LocationCallback(){
        @SuppressLint("NewApi")
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            for(location in p0.locations){
                if(location!=null){
                    Log.d(TAG, "GetLocationWeatherActivity current location: ${location.latitude} ${location.longitude}")
                    val geoCoder = Geocoder(applicationContext)
                    val currentLocation = geoCoder.getFromLocation(location.latitude, location.longitude,1)
                    locationWeather.latLocation = location.latitude
                    locationWeather.longLocation = location.longitude

                    if(currentLocation!=null){
                        locationWeather.nameLocation = currentLocation.first().subAdminArea + "-" +currentLocation.first().adminArea
                    }

                    saveLocationWeather()
                }
            }
        }
    }

    private fun checkSettingAndLocationUpdate() {
        val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
        val settingsClient = LocationServices.getSettingsClient(this)

        val task = settingsClient.checkLocationSettings(locationSettingsRequest)

        task.addOnSuccessListener {
            startLocationUpdate()
        }

        task.addOnFailureListener{
            if(it is ResolvableApiException){
                val apiException = it
                apiException.startResolutionForResult(this, 1001)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }



    private fun getLocationByAddress(){
        Log.d(TAG, "GetLocationWeatherActivity getLocationByAddress Start: ${locationWeather.nameLocation} ${locationWeather.latLocation} ${locationWeather.longLocation}")

        val addressLocation: String = edtLocation.text.toString().trim()
        if(Geocoder.isPresent()){
            var geocoder = Geocoder(applicationContext)
            if(addressLocation.isNotEmpty()){
                try {
                    val location = geocoder.getFromLocationName(addressLocation, 1)

                    if (location.size>0 && location!=null){
                        locationWeather.nameLocation=addressLocation
                        locationWeather.latLocation=location.first().latitude
                        locationWeather.longLocation=location.first().longitude
                        Log.d(TAG, "GetLocationWeatherActivity getLocationByAddress end: ${locationWeather.nameLocation} ${locationWeather.latLocation} ${locationWeather.longLocation}")
                        saveLocationWeather()
                    }
                }catch (e : Exception){
                    Log.d(TAG, "GetLocationWeatherActivity getLocationByAddress Exceptions: ${e.message}")
                }
            }
        }
    }

    // PERMISSION:
    private fun checkPermissionLocation(){
        if (ActivityCompat.checkSelfPermission( this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permission granted
            permissionLocationCheck=true
        } else {
            askLocationPermission()
        }
    }

    private fun askLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                // show dialog
            }
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQEST_CODE) {
            permissionLocationCheck = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }
}