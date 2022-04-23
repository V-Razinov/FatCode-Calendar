package com.mediasoft.fatcode_calendar.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mediasoft.fatcode_calendar.databinding.PageMonthDaysBinding

class MonthDaysAdapter : RecyclerView.Adapter<MonthDaysViewHolder>() {

    private val months = mutableListOf<Month>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthDaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MonthDaysViewHolder(
            binding = PageMonthDaysBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MonthDaysViewHolder, position: Int) {
        holder.bind(months[position])
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
    private val binding: PageMonthDaysBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(month: Month) {
        binding.root.setMonth(month)
    }
}