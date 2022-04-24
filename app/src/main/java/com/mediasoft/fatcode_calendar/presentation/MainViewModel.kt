package com.mediasoft.fatcode_calendar.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mediasoft.fatcode_calendar.data.net.WeatherSuccessResponse
import com.mediasoft.fatcode_calendar.domain.LoadWeatherUseCase
import com.mediasoft.fatcode_calendar.other.capitalized
import com.mediasoft.fatcode_calendar.other.custom.Day
import com.mediasoft.fatcode_calendar.other.custom.Month
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class MainViewModel : ViewModel() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    private val titleMonthDateFormat = SimpleDateFormat("LLLL", Locale.getDefault())
    private val selectionDateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())

    val months = MutableLiveData<List<Month>>(emptyList())
    val weather = MutableLiveData<List<BaseWeatherItem>>(emptyList())
    val currentMonth = MutableLiveData("")
    val currentSelection = MutableLiveData("")

    init {
        scope.launch {
            months.value = buildMonths()
        }
    }

    fun monthSelected(monthOrder: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthOrder)
        currentMonth.value = titleMonthDateFormat
            .format(calendar.time)
            .capitalized
    }

    fun setSelectionDates(
        startMonth: Month?,
        endMonth: Month?,
        startDay: Day?,
        endDay: Day?
    ) {
        currentSelection.value = when {
            startMonth == null -> null
            else -> buildString {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.MONTH, startMonth.order)
                calendar.set(Calendar.DAY_OF_MONTH, startDay!!.day)
                append(selectionDateFormat.format(calendar.time))
                if (endMonth != null) {
                    append(" - ")
                    calendar.set(Calendar.MONTH, endMonth.order)
                    calendar.set(Calendar.DAY_OF_MONTH, endDay!!.day)
                    append(selectionDateFormat.format(calendar.time))
                }
            }
        }
        if (startMonth != null && endMonth != null)
            requestWeatherForRange(startMonth, startDay!!, endMonth, endDay!!)
        else
            weather.value = emptyList()
    }

    private fun requestWeatherForRange(
        startMonth: Month,
        startDay: Day,
        endMonth: Month,
        endDay: Day
    ) {
        //не нашел бесплатный api с возможность погрузки даныых по диапазону
        //грузяться данные до конца диапазона
        scope.launch {
            weather.value = listOf(BaseWeatherItem.Loader)
            val userCase = LoadWeatherUseCase(
                upToDate = Calendar.getInstance().apply {
                    set(Calendar.MONTH, endMonth.order)
                    set(Calendar.DAY_OF_MONTH, endDay.day)
                },
                dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            )
            weather.value = when (val result = userCase.execute()) {
                is LoadWeatherUseCase.Result.Error -> listOf(result.message)
                is LoadWeatherUseCase.Result.Success -> result.items
            }
        }
    }

    private fun buildMonths(): List<Month> {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val months = mutableListOf<Month>()
        repeat(12) {
            calendar.set(Calendar.MONTH, it)
            months.add(Month.from(calendar))
        }
        return months.toList()
    }
}