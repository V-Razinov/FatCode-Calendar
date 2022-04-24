package com.mediasoft.fatcode_calendar.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.mediasoft.fatcode_calendar.databinding.ActivityMainBinding
import com.mediasoft.fatcode_calendar.other.CalendarSelection
import com.mediasoft.fatcode_calendar.other.capitalized
import com.mediasoft.fatcode_calendar.other.custom.Day
import com.mediasoft.fatcode_calendar.other.custom.Month
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val titleMonthDateFormat = SimpleDateFormat("LLLL", Locale.getDefault())

    private val calendarSelection = CalendarSelection()
    private val selectionDateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
    private val selectionObserver = CalendarSelection.Observer { startMonth, startDay, endMonth, endDay ->
        setSelectionDates(startMonth, endMonth, startDay, endDay)
    }

    private val monthsAdapter = MonthDaysAdapter(calendarSelection = calendarSelection)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarSelection.observe(selectionObserver)

        binding.months.adapter = monthsAdapter
        binding.months.registerOnPageChangeCallback(getOnPageChangeCallback())

        monthsAdapter.setMonths(months = buildMonths())
        binding.months.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH), false)
    }

    private fun buildMonths(): MutableList<Month> {
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
        return months
    }

    private fun getOnPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        val calendar = Calendar.getInstance()

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            calendar.set(Calendar.MONTH, position)
            binding.toolbar.title =
                titleMonthDateFormat
                    .format(calendar.time)
                    .capitalized
        }
    }

    private fun setSelectionDates(
        startMonth: Month?,
        endMonth: Month?,
        startDay: Day?,
        endDay: Day?
    ) {
        binding.dateSelection.text = when {
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
    }
}