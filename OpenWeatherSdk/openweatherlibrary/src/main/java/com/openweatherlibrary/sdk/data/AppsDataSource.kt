package com.openweatherlibrary.sdk.data


import com.openweatherlibrary.sdk.data.model.weather.todaymodels.WeatherResponse
import com.openweatherlibrary.sdk.data.model.weather.weekmodels.WeatherWeekResponse
import com.openweatherlibrary.sdk.data.network.ApiRestServiceProvider
import com.openweatherlibrary.sdk.data.network.ApiService
import io.reactivex.Single

internal class AppsDataSource : DataSource {
    private val apiService: ApiService =
        ApiRestServiceProvider().provideApiService()

    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        apiKey: String
    ): Single<WeatherResponse> {
        return apiService.getCurrentWeather(latitude, longitude, tempUnit, apiKey)
    }

    override fun getCurrentWeatherForWeek(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        exclude: String,
        cnt: Int,
        apiKey: String
    ): Single<WeatherWeekResponse> {
        return apiService.getCurrentWeatherforWeek(latitude, longitude, tempUnit, exclude, cnt, apiKey)
    }
}