package com.mediasoft.fatcode_calendar.data.net

import com.google.gson.annotations.SerializedName

data class WeatherSuccessResponse(
    val timezoneOffset: Int,
    val daily: List<Daily>
) {
    data class Daily(
        @SerializedName("dt")
        val dateUnix: Int,
        @SerializedName("temp")
        val temperature: Temp,
        val weather: List<Weather>?,
    )

    data class Temp(
        val min: Double,
        val max: Double,
    )

    data class Weather(
        val description: String,
        val icon: String,
    )
}