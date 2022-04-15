package com.openweatherlibrary.sdk.data

import com.openweatherlibrary.sdk.data.model.weather.todaymodels.WeatherResponse
import com.openweatherlibrary.sdk.data.model.weather.weekmodels.WeatherWeekResponse
import io.reactivex.Single

internal interface DataSource {

    fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        apiKey: String
    ): Single<WeatherResponse>

    fun getCurrentWeatherForWeek(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        exclude: String,
        cnt: Int,
        apiKey: String
    ): Single<WeatherWeekResponse>

}