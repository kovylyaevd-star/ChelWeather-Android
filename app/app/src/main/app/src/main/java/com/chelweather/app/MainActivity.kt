package com.chelweather.app

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.errorText)
        
        adapter = WeatherAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        
        loadWeather()
    }

    private fun loadWeather() {
        progressBar.visibility = View.VISIBLE
        errorText.visibility = View.GONE
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherList = WeatherParser.getWeather()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (weatherList.isNotEmpty()) {
                        adapter.submitList(weatherList)
                    } else {
                        showError("Не удалось загрузить данные. Проверьте интернет или структуру сайта.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Ошибка: ${e.message}")
                }
            }
        }
    }

    private fun showError(msg: String) {
        progressBar.visibility = View.GONE
        errorText.text = msg
        errorText.visibility = View.VISIBLE
    }
}
