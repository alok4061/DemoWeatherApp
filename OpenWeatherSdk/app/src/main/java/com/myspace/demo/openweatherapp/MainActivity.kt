package com.myspace.demo.openweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.myspace.demo.openweatherapp.databinding.ActivityMainBinding
import com.openweatherlibrary.sdk.WeatherSDK
import com.openweatherlibrary.sdk.data.model.weather.todaymodels.WeatherResponse
import com.openweatherlibrary.sdk.data.model.weather.weekmodels.WeatherWeekResponse
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {

    private lateinit var weatherSDK: WeatherSDK
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var binding: ActivityMainBinding
    private var latitude = 0.0
    private var longitude = 0.0
    private var exclude = "hourly,minutely"
    private var cnt = 7
    private var timeUnit =  WeatherSDK.TempUnit.CELSIUS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        weatherSDK = WeatherSDK.getInstance(getString(R.string.key))
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        binding.toolbar.title = resources.getString(R.string.app_name)
        binding.content.degreeCelsius.isChecked = true
        binding.content.radioGroup?.setOnCheckedChangeListener { radioGroup, _ ->
            val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
            val radioButton: RadioButton = binding.root.findViewById(intSelectButton)
            if (radioButton.text.toString().equals(resources.getString(R.string.degreeCelsius))) {
                timeUnit = WeatherSDK.TempUnit.CELSIUS
            } else {
                timeUnit = WeatherSDK.TempUnit.FAHRENHEIT
            }

        }

        binding.content.today.setOnClickListener {
            getCurrentWeatherToday()
        }

        binding.content.week.setOnClickListener {
            getCurrentWeatherForWeek()
        }

    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                goToLocationSettings()
            }
        }
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() { // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                fusedLocationProviderClient?.lastLocation?.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        setLocation(location)
                    }

                }
            } else {
                goToLocationSettings()

            }
        } else {
// if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }

    private fun goToLocationSettings() {
        Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG)
            .show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun setLocation(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        binding.content.tvLocation.text =
            "latitude: ${location?.latitude}, longitude: ${location?.longitude} "
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            setLocation(mLastLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }


    /**
     * show the today weather information
     */
    private fun getCurrentWeatherToday() {
        weatherSDK.getCurrentWeatherToday(latitude, longitude,
            timeUnit,
            object : WeatherSDK.WeatherDataListener {
                override fun onWeatherResponse(response: WeatherResponse) {
                    showAlertDialog(response.toString())
                }

                override fun onErrorFetchingData(error: Throwable) {
                    showAlertDialog(error.message.toString())
                }
            })

    }

    /**
     * show the week weather information
     */
    private fun getCurrentWeatherForWeek() {
        weatherSDK.getCurrentWeatherForWeek(latitude, longitude,
            timeUnit,
            exclude,
            cnt,
            object : WeatherSDK.WeatherDataListenerforWeek {
                override fun onWeatherResponseforWeek(response: WeatherWeekResponse) {
                    showAlertDialog(response.toString())
                }

                override fun onErrorFetchingData(error: Throwable) {
                    showAlertDialog(error.message.toString())
                }
            })

    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val PERMISSION_ID = 44
    }


    fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        var alertDialog: AlertDialog? = null
        builder.setTitle("Weather Data")
        builder.setMessage(message)

        //performing positive action
        builder.setPositiveButton("Close") { _, _ ->
            alertDialog?.dismiss()
        }
        alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun formatString(text: String): String? {
        val json = StringBuilder()
        var indentString = ""
        for (i in 0 until text.length) {
            val letter = text[i]
            when (letter) {
                '{', '[' -> {
                    json.append(
                        """
                        
                        $indentString$letter
                        
                        """.trimIndent()
                    )
                    indentString = indentString + "\t"
                    json.append(indentString)
                }
                '}', ']' -> {
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append(
                        """
                        
                        $indentString$letter
                        """.trimIndent()
                    )
                }
                ',' -> json.append(
                    """
                    $letter
                    $indentString
                    """.trimIndent()
                )
                else -> json.append(letter)
            }
        }
        return json.toString()
    }

}