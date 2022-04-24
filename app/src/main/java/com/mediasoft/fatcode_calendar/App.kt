package com.mediasoft.fatcode_calendar

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.mediasoft.fatcode_calendar.data.net.retrofit.RetrofitService
import com.mediasoft.fatcode_calendar.data.net.retrofit.WeatherApi

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        weatherApi = RetrofitService.weatherApi
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
        lateinit var weatherApi: WeatherApi
            private set
    }
}