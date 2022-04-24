package com.mediasoft.fatcode_calendar.domain

import com.mediasoft.fatcode_calendar.App
import com.mediasoft.fatcode_calendar.data.net.WeatherSuccessResponse
import com.mediasoft.fatcode_calendar.data.net.retrofit.IMAGE_URL
import com.mediasoft.fatcode_calendar.other.isNetworkAvailable
import com.mediasoft.fatcode_calendar.presentation.BaseWeatherItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LoadWeatherUseCase(
    private val upToDate: Calendar,
    private val dateFormat: SimpleDateFormat,
    private val latLng: Pair<Double, Double> = 54.32824 to 48.38657,//Ульяновск
) {

    suspend fun execute(): Result {
        if (!App.context.isNetworkAvailable)
            return Result.Error(BaseWeatherItem.Error("Нет интернета"))

        return withContext(Dispatchers.IO) {
            try {
                handleResponse(App.weatherApi.getWeatherByLatLng(latLng.first, latLng.second))
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(
                    BaseWeatherItem.Error(
                        e.localizedMessage ?: e.message ?: "Не удалось загрузить"
                    )
                )
            }
        }
    }

    private fun handleResponse(
        response: Response<WeatherSuccessResponse>,
    ): Result {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body == null) {
                Result.Error(BaseWeatherItem.Error("Не удалось загрузить"))
            } else {
                Result.Success(mapResponse(body))
            }
        } else {
            return Result.Error(BaseWeatherItem.Error("Не удалось загрузить"))
        }
    }

    private fun mapResponse(items: WeatherSuccessResponse): List<BaseWeatherItem.WeatherItem> {
        val calendar = Calendar.getInstance()
        return items.daily
            .takeWhile { daily ->
                val dailyDate = calendar.apply {
                    timeInMillis = daily.dateUnix * 1000L
                }
                dailyDate < upToDate
            }
            .map { daily ->
                val weather = daily.weather?.firstOrNull()
                BaseWeatherItem.WeatherItem(
                    date = calendar.run {
                        timeInMillis = daily.dateUnix * 1000L
                        dateFormat.format(time).toString()
                    },
                    description = weather?.description
                        ?.replaceFirstChar { it.uppercase() }
                        ?: "",
                    temperature = "${daily.temperature.min.toInt()}° / " +
                            "${daily.temperature.max.toInt()}°",
                    icon = weather?.icon?.let { "$IMAGE_URL/img/wn/$it@2x.png" } ?: ""
                )
            }
    }

    sealed class Result {

        data class Success(val items: List<BaseWeatherItem.WeatherItem>) : Result()

        data class Error(val message: BaseWeatherItem.Error) : Result()
    }
}