package com.mediasoft.fatcode_calendar.other.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.updatePadding
import com.mediasoft.fatcode_calendar.other.toDp
import kotlin.math.absoluteValue

typealias DayView = TextView
typealias WeekView = TableRow

class MonthDaysView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TableLayout(context, attrs) {

    var onDayClick: ((Day) -> Unit)? = null

    private var month: Month? = null
    private val defaultDayColor = Color.WHITE
    private val selectedDayColor = Color.GREEN

    init {
        isStretchAllColumns = true
        addView(createWeekDayNamesView())
    }

    fun setMonth(month: Month?) {
        this.month = month
        if (month == null)
            clear()
        else
            fillDays()
    }

    fun select(
        startMonth: Month? = null,
        startDay: Day? = null,
        endMonth: Month? = null,
        endDay: Day? = null
    ) {
        //Диапазон отсутствует
        if (startMonth == null && endMonth == null) {
            unselect()
            return
        }
        val month = month ?: run {
            unselect()
            return
        }

        //Выбран один день не в тукщем месяце
        if (startMonth != null && endMonth == null && startMonth != month) {
            unselect()
        }

        //Выбран день в текущем месяце
        if ((startMonth != null && startMonth == month) || (endMonth != null && endMonth == month)) {
            select(startDay ?: endDay ?: return)
        }

        //выборка в текущем месяце
        if (startMonth == endMonth && startMonth == month && startDay != null && endDay != null) {
            selectFromTo(startDay, endDay)
            return
        }

        //Выборка с границами
        if (startMonth != null && startDay != null && endMonth != null && endDay != null) {
            when {
                //Месяц между диапазоном
                startMonth < month && endMonth > month -> {
                    selectAll()
                }
                //Правая граница диапазлона в текущем месяце
                startMonth < month && endMonth == month -> {
                    selectUpTo(endDay)
                }
                //Левая граница диапазлона в текущем месяце
                startMonth == month && endMonth > month -> {
                    selectDownTo(startDay)
                }
                else -> {
                    unselect()
                }
            }
            return
        }
    }

    private fun selectAll() {
        forEachDay { dayView ->
            dayView.select(true)
        }
    }

    private fun select(day: Day) {
        forEachDay { dayView ->
            dayView.select(dayView.text.toString() == day.day.toString())
        }
    }

    fun unselect() {
        forEachDay { dayView ->
            dayView.select(false)
        }
    }

    private fun selectUpTo(day: Day) {
        forEachDay { dayView ->
            dayView.select(dayView.text.toString().toIntOrNull() ?: -1 <= day.day)
        }
    }

    private fun selectDownTo(day: Day) {
        forEachDay { dayView ->
            dayView.select(dayView.text.toString().toIntOrNull() ?: -1 >= day.day)
        }
    }

    private fun selectFromTo(startDay: Day, endDay: Day) {
        val range = startDay.day..endDay.day
        forEachDay { dayView ->
            dayView.select(dayView.text?.toString()?.toIntOrNull() in range)
        }
    }

    private fun fillDays() {
        val month = month ?: return
        val childCount = childCount - 1
        val newSize = month.weeks.size + 1

        val diff = (childCount - newSize).absoluteValue
        if (childCount > newSize)
            removeViews(childCount - diff, diff)
        else if (childCount < newSize)
            repeat(diff) { addView(createWeekView()) }

        repeat(newSize) { weekIndex ->
            val week = month.weeks.getOrNull(weekIndex)
            (getChildAt(weekIndex + 1) as ViewGroup)
                .children
                .forEachIndexed dayEach@{ dayIndex, day ->
                    if (day !is TextView) return@dayEach

                    val d = week?.days
                        ?.getOrNull(dayIndex)
                        ?.takeIf { it.day > 0 }
                    day.text = d?.day?.toString()

                    if (d != null)
                        day.setOnClickListener {
                            onDayClick?.invoke(d)
                        }
                    else
                        day.setOnClickListener(null)
                }
        }
    }

    private inline fun forEachDay(receiver: (dayView: DayView) -> Unit) {
        children
            .toList()
            .subList(1, childCount - 1)
            .forEach { weekView ->
                weekView as WeekView
                weekView.children.forEach { dayView ->
                    receiver(dayView as TextView)
                }
            }
    }

    private fun clear() {
        removeViews(1, childCount)
    }

    private fun createWeekDayNamesView() = WeekView(context).apply {
        addView(createDayView().apply { text = "пн" })
        addView(createDayView().apply { text = "вт" })
        addView(createDayView().apply { text = "ср" })
        addView(createDayView().apply { text = "чт" })
        addView(createDayView().apply { text = "пт" })
        addView(createDayView().apply { text = "сб" })
        addView(createDayView().apply { text = "вс" })
    }

    private fun createWeekView() = WeekView(context).apply {
        updatePadding(
            top = 8.toDp(context).toInt(),
            bottom = 8.toDp(context).toInt()
        )
        repeat(7) {
            addView(createDayView())
        }
    }

    private fun createDayView() = DayView(context).apply {
        this.gravity = Gravity.CENTER
        select(false)
    }

    private fun DayView.select(selected: Boolean) {
        setTextColor(
            if (selected) selectedDayColor else defaultDayColor
        )
    }
}