package com.mediasoft.fatcode_calendar.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mediasoft.fatcode_calendar.databinding.ItemErrorBinding
import com.mediasoft.fatcode_calendar.databinding.ItemLoaderBinding
import com.mediasoft.fatcode_calendar.databinding.ItemWeatherBinding

class WeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_WEATHER = 0
    private val VIEW_TYPE_ERROR = 1
    private val VIEW_TYPE_LOADER = 2

    private val items = mutableListOf<BaseWeatherItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_WEATHER -> WeatherViewHolder(
                binding = ItemWeatherBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_LOADER -> LoaderViewHolder(
                binding = ItemLoaderBinding.inflate(inflater, parent, false)
            )
            else -> ErrorViewHolder(
                binding = ItemErrorBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WeatherViewHolder -> holder.bind(items[position] as BaseWeatherItem.WeatherItem)
            is ErrorViewHolder -> holder.bind(items[position] as BaseWeatherItem.Error)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is BaseWeatherItem.Error -> VIEW_TYPE_ERROR
        is BaseWeatherItem.WeatherItem -> VIEW_TYPE_WEATHER
        BaseWeatherItem.Loader -> VIEW_TYPE_LOADER
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setWeather(items: List<BaseWeatherItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}

class WeatherViewHolder(
    private val binding: ItemWeatherBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BaseWeatherItem.WeatherItem) = with(binding) {
        temperature.text = item.temperature
        date.text = item.date
        weatherDescription.text = item.description
        Glide
            .with(binding.root)
            .load(item.icon)
            .into(binding.weatherIcon)
    }
}

class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BaseWeatherItem.Error) = with(binding.root) {
        text = item.message
    }
}

class LoaderViewHolder(
    private val binding: ItemLoaderBinding
) : RecyclerView.ViewHolder(binding.root)