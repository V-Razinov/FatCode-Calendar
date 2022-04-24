package com.mediasoft.fatcode_calendar.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mediasoft.fatcode_calendar.databinding.PageMonthDaysBinding
import com.mediasoft.fatcode_calendar.other.CalendarSelection
import com.mediasoft.fatcode_calendar.other.custom.Day
import com.mediasoft.fatcode_calendar.other.custom.Month

class MonthDaysAdapter(
    private val calendarSelection: CalendarSelection
) : RecyclerView.Adapter<MonthDaysViewHolder>() {

    private val months = mutableListOf<Month>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthDaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MonthDaysViewHolder(
            binding = PageMonthDaysBinding.inflate(inflater, parent, false),
            onDayClick = calendarSelection::select,
        )
    }

    override fun onBindViewHolder(holder: MonthDaysViewHolder, position: Int) {
        holder.bind(months[position])
        calendarSelection.observe(holder.selectionObserver)
    }

    override fun onViewRecycled(holder: MonthDaysViewHolder) {
        super.onViewRecycled(holder)
        calendarSelection.removeObserver(holder.selectionObserver)
    }

    override fun getItemCount(): Int = months.size

    @SuppressLint("NotifyDataSetChanged")
    fun setMonths(months: List<Month>) {
        this.months.clear()
        this.months.addAll(months)
        notifyDataSetChanged()
    }

}

class MonthDaysViewHolder(
    private val binding: PageMonthDaysBinding,
    private val onDayClick: (month: Month, day: Day) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    val selectionObserver = CalendarSelection.Observer { startMonth, startDay, endMonth, endDay ->
        binding.root.select(startMonth, startDay, endMonth, endDay)
    }

    fun bind(month: Month) {
        binding.root.setMonth(month)
        binding.root.onDayClick = { day: Day -> onDayClick(month, day) }
    }
}