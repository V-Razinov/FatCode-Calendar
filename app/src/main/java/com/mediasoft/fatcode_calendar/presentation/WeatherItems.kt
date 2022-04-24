package com.mediasoft.fatcode_calendar.presentation


sealed class BaseWeatherItem {

    data class WeatherItem(
        val date: String,
        val description: String,
        val temperature: String,
        val icon: String,
    ) : BaseWeatherItem()

    data class Error(val message: String) : BaseWeatherItem()

    object Loader: BaseWeatherItem()
}