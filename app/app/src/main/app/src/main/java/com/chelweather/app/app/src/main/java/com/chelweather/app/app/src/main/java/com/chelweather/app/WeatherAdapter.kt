package com.chelweather.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private var items = listOf<WeatherDay>()

    fun submitList(list: List<WeatherDay>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = items[position]
        holder.dateText.text = item.date
        holder.tempText.text = "День: ${item.tempDay}° / Ночь: ${item.tempNight}°"
        holder.conditionText.text = item.condition
        holder.windText.text = "Ветер: ${item.wind}"
    }

    override fun getItemCount() = items.size

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val tempText: TextView = itemView.findViewById(R.id.tempText)
        val conditionText: TextView = itemView.findViewById(R.id.conditionText)
        val windText: TextView = itemView.findViewById(R.id.windText)
    }
}
