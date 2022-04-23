package com.mediasoft.fatcode_calendar.presentation

import android.content.Context
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

class MonthDaysView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TableLayout(context, attrs) {

    private var month: Month? = null
    private val weekDayNamesRow: TableRow

    init {
        isStretchAllColumns = true
        weekDayNamesRow = createWeekDayNamesView()
        addView(weekDayNamesRow)
    }

    fun setMonth(month: Month?) {
        this.month = month
        if (month == null)
            clear()
        else
            fillDays()
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

        repeat(newSize) { index ->
            val week = month.weeks.getOrNull(index)
            (getChildAt(index + 1) as ViewGroup)
                .children
                .forEachIndexed dayEach@{ dayIndex, day ->
                    if (day !is TextView) return@dayEach

                    day.text = week?.days
                        ?.getOrNull(dayIndex)
                        ?.day
                        ?.takeIf { it > 0 }
                        ?.toString()
                }
        }
    }

    private fun clear() {
        removeViews(1, childCount)
    }

    private fun createWeekDayNamesView() = TableRow(context).apply {
        addView(createDayView().apply { text = "пн" })
        addView(createDayView().apply { text = "вт" })
        addView(createDayView().apply { text = "ср" })
        addView(createDayView().apply { text = "чт" })
        addView(createDayView().apply { text = "пт" })
        addView(createDayView().apply { text = "сб" })
        addView(createDayView().apply { text = "вс" })

    }

    private fun createWeekView() = TableRow(context).apply {
        updatePadding(
            top = 8.toDp(context).toInt(),
            bottom = 8.toDp(context).toInt()
        )
        repeat(7) {
            addView(createDayView())
        }
    }

    private fun createDayView() = TextView(context).apply {
        this.gravity = Gravity.CENTER
    }
}