package com.mediasoft.fatcode_calendar.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.mediasoft.fatcode_calendar.databinding.ActivityMainBinding
import com.mediasoft.fatcode_calendar.other.capitalized
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val monthsAdapter = MonthDaysAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                SimpleDateFormat(
                    "LLLL",
                    Locale.getDefault()
                )
                    .format(calendar.time)
                    .capitalized
        }
    }
}