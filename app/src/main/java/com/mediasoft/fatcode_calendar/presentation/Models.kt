package com.mediasoft.fatcode_calendar.presentation

import java.util.*

@JvmInline
value class Day(val day: Int)

@JvmInline
value class Week(val days: List<Day>)

data class Month(
    val order: Int,
    val weeks: List<Week>
) {
    companion object {
        fun from(calendar: Calendar): Month {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val days = mutableListOf<Day>().apply {
                repeat(dayOfWeek - 1) {
                    add(Day(-1))
                }
                repeat(daysInMonth) {
                    add(Day(it + 1))
                }

            }
            return Month(
                order = calendar.get(Calendar.MONTH),
                days.chunked(7).map { Week(it) }
            )
        }
    }
}