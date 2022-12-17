package com.example.rifsa_mobile.view.fragment.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.WeatherForecastResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.WeatherDetailRequest
import com.example.rifsa_mobile.model.entity.openweatherapi.request.WeatherForecastRequest
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.repository.local.LocalRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteWeatherRepository

class WeatherFragmentViewModel(
    private val weatherRepository: RemoteWeatherRepository,
    private val localRepository: LocalRepository
    ): ViewModel() {

    fun getUserLocation():LiveData<List<Double>> =
        localRepository.getLocationUser()

    suspend fun getWeatherDataBySearch(location : String)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataBySearch(location)

    suspend fun getWeatherForecastData(request: WeatherForecastRequest)
    : LiveData<FetchResult<WeatherForecastResponse>> =
        weatherRepository.getWeatherForecastData(request)

    suspend fun getWeatherDataByLocation(request : WeatherDetailRequest)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataByLocation(request)
}