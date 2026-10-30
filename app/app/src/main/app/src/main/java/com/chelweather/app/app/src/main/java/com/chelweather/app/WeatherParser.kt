package com.chelweather.app

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

data class WeatherDay(
    val date: String,
    val tempDay: String,
    val tempNight: String,
    val condition: String,
    val wind: String
)

object WeatherParser {
    private const val URL = "https://www.chelpogoda.ru"
    
    suspend fun getWeather(): List<WeatherDay> {
        val result = mutableListOf<WeatherDay>()
        
        try {
            val doc: Document = Jsoup.connect(URL)
                .userAgent("Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36")
                .timeout(15000)
                .followRedirects(true)
                .get()
            
            // ⚠️ ВАЖНО: Проверьте селекторы на сайте!
            // Откройте chelpogoda.ru в браузере → F12 → найдите классы блоков прогноза
            val days = doc.select("div.forecast-day, .day-block, .weather-item, .forecast-item")
            
            for (day in days) {
                val date = extractText(day, ".date, .day-title, .forecast-date", "Дата не указана")
                val tempDay = extractText(day, ".temp-day, .day-temp, .temperature-day", "—")
                val tempNight = extractText(day, ".temp-night, .night-temp, .temperature-night", "—")
                val condition = extractText(day, ".condition, .desc, .weather-desc", "—")
                val wind = extractText(day, ".wind, .wind-speed", "—")
                
                result.add(WeatherDay(date, tempDay, tempNight, condition, wind))
            }
            
            // Если селекторы не сработали, попробуем альтернативный вариант
            if (result.isEmpty()) {
                // Попробуем найти таблицу или другие контейнеры
                val allText = doc.select("body").text()
                // Здесь можно добавить логирование для отладки
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Ошибка загрузки: ${e.message}")
        }
        
        return result
    }
    
    private fun extractText(element: Element, selector: String, default: String): String {
        val found = element.select(selector).first()
        return found?.text()?.trim() ?: default
    }
}
