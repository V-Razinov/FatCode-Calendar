package com.mediasoft.fatcode_calendar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.updatePadding

class MonthDaysView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TableLayout(context, attrs) {

    private val weeksCount = 5
    private var month: Month? = null
    private val weekDayNamesRow: TableRow
    private val weeksRows: List<TableRow>

    init {
        isStretchAllColumns = true
        weekDayNamesRow = createWeekDayNamesView()
        addView(weekDayNamesRow)
        weeksRows = mutableListOf<TableRow>().apply {
            repeat(weeksCount) {
                val view = createWeekView()
                addView(view)
                add(view)
            }
        }
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
        weeksRows.forEachIndexed rowEach@{ rowIndex, row ->
            val week = month.weeks.getOrNull(rowIndex)

            row.children.forEachIndexed dayEach@{ index, day ->
                if (day !is TextView) return@dayEach

                day.text = week?.days
                    ?.getOrNull(index)
                    ?.day
                    ?.takeIf { it >= 0 }
                    ?.toString()
            }
        }
    }

    private fun clear() {
        weeksRows.forEach rowEach@{ row ->
            row.children.forEach dayEach@{ day ->
                if (day !is TextView) return@dayEach
                day.text = null
            }
        }
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