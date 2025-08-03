package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject



class MainActivity : AppCompatActivity() {

    private val apiKey = "e1e90a83cd2bfcec876d6ee5ab066623" // вставьте сюда ваш ключ OpenWeatherMap
    private val city = "Moscow"
    private lateinit var weatherText: TextView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherText = findViewById(R.id.weatherText)

        fetchWeather()
    }

    private fun fetchWeather() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url =
                    "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric&lang=ru"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val jsonData = response.body?.string()

                if (jsonData != null) {
                    val jsonObject = JSONObject(jsonData)
                    val temp = jsonObject.getJSONObject("main").getDouble("temp")
                    val description =
                        jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")

                    val displayText = "Погода в Москве:\n$temp°C, $description"

                    withContext(Dispatchers.Main) {
                        weatherText.text = displayText
                    }
                } else {
                    showError("Ошибка получения данных")
                }
            } catch (e: Exception) {
                showError("Ошибка: ${e.message}")
            }
        }
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            weatherText.text = message
        }
    }
}


