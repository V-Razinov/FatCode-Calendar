package com.mediasoft.fatcode_calendar.other

import android.util.Log
import com.mediasoft.fatcode_calendar.other.custom.Day
import com.mediasoft.fatcode_calendar.other.custom.Month

class CalendarSelection {
    var startMonth: Month? = null
        private set
    var startDay: Day? = null
        private set
    var endMonth: Month? = null
        private set
    var endDay: Day? = null
        private set

    private val observers = mutableSetOf<Observer>()

    fun select(month: Month, day: Day) {
        when {
            startMonth == null -> {
                startMonth = month
                startDay = day
            }
            startMonth == month && startDay == day -> {
                startMonth = endMonth
                startDay = endDay
                endMonth = null
                endDay = null
            }
            endMonth == month && endDay == day -> {
                endMonth = null
                endDay = null
            }
            endMonth == null -> {
                endMonth = month
                endDay = day
                if (endMonth!! <= startMonth!! && endDay!! < startDay!!) {
                    val tempMonth = startMonth
                    val tempDay = startDay
                    startMonth = endMonth
                    startDay = endDay
                    endMonth = tempMonth
                    endDay = tempDay
                }
            }
            else -> {
                startMonth = month
                startDay = day
                endMonth = null
                endDay = null
            }
        }
        log()
        notifyObservers()
    }


    fun observe(observer: Observer) {
        observers.add(observer)
        observer.onChanged(startMonth, startDay, endMonth, endDay)
    }

    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    private fun log() {
        Log.d(
            "CalendarSelection", "selection:  start: ${startMonth?.order}-${startDay?.day}, " +
                    "end: ${endMonth?.order}-${endDay?.day}"
        )
    }

    private fun notifyObservers() {
        observers.forEach { observer ->
            observer.onChanged(startMonth, startDay, endMonth, endDay)
        }
    }

    fun interface Observer {
        fun onChanged(
            startMonth: Month?,
            startDay: Day?,
            endMonth: Month?,
            endDay: Day?
        )
    }
}