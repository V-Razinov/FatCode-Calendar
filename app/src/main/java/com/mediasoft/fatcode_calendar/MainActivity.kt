package com.mediasoft.fatcode_calendar

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mediasoft.fatcode_calendar.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.selectDate.setOnClickListener {
            showPickDateDialog()
        }
        val calendar = Calendar.getInstance()
        setCalendar(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH)
        )
    }

    private fun setCalendar(year: Int, month: Int) {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        binding.monthName.text =
            SimpleDateFormat(
                "LLLL",
                Locale.getDefault()
            )
                .format(calendar.time)
                .capitalized
        binding.monthView.setMonth(Month.from(calendar))
    }

    private fun showPickDateDialog() {
        DatePickerDialog(this).apply {
            setOnDateSetListener { datePicker, i, i2, i3 ->
                setCalendar(datePicker.year, datePicker.month)
            }
            show()
        }
    }
}