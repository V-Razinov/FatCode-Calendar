package com.mediasoft.fatcode_calendar.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.mediasoft.fatcode_calendar.databinding.ActivityMainBinding
import com.mediasoft.fatcode_calendar.other.CalendarSelection
import com.mediasoft.fatcode_calendar.other.capitalized
import com.mediasoft.fatcode_calendar.other.custom.Day
import com.mediasoft.fatcode_calendar.other.custom.Month
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val calendarSelection = CalendarSelection()
    private val selectionObserver = object : CalendarSelection.Observer {
        override fun onChanged(startMonth: Month?, startDay: Day?, endMonth: Month?, endDay: Day?) {
            viewModel.setSelectionDates(startMonth, endMonth, startDay, endDay)
        }
    }

    private val monthsAdapter = MonthDaysAdapter(calendarSelection = calendarSelection)
    private val weatherAdapter = WeatherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[MainViewModel::class.java]

        calendarSelection.observe(selectionObserver)

        binding.months.adapter = monthsAdapter
        binding.months.registerOnPageChangeCallback(getOnPageChangeCallback())

        binding.weather.adapter = weatherAdapter
        binding.weather.layoutManager = LinearLayoutManager(this)

        observeViewModel()
    }

    private fun observeViewModel() = with(viewModel) {
        months.observe(this@MainActivity) { months ->
            monthsAdapter.setMonths(months = months)
            binding.months.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH), false)
        }
        weather.observe(this@MainActivity, weatherAdapter::setWeather)
        currentMonth.observe(this@MainActivity, binding.toolbar::setTitle)
        currentSelection.observe(this@MainActivity, binding.dateSelection::setText)
    }

    private fun getOnPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.monthSelected(position)
        }
    }
}