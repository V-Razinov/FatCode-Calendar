package com.mediasoft.fatcode_calendar.data.net.retrofit

import com.mediasoft.fatcode_calendar.data.net.WeatherSuccessResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET(
        "/data/2.5/onecall?" +
                "&appid=$API_KEY" +
                "&exclude=hourly,minutely,alerts,current&units=metric" +
                "&lang=ru"
    )
    suspend fun getWeatherByLatLng(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
    ) : Response<WeatherSuccessResponse>
}