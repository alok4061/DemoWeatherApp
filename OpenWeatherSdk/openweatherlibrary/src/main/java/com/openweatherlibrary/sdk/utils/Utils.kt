package com.openweatherlibrary.sdk.utils

import com.openweatherlibrary.sdk.WeatherSDK

object Utils {
    fun getConvertedUnit(tempUnit: WeatherSDK.TempUnit): String {
        return when (tempUnit.name) {
            WeatherSDK.TempUnit.CELSIUS.name -> {
                "metric"
            }
            WeatherSDK.TempUnit.FAHRENHEIT.name -> {
                "imperial"
            }
            else -> {
                "standard"
            }
        }
    }

}