package com.weather.bratislava.data.utils

import com.weather.bratislava.data.models.DayForecast
import com.weather.bratislava.data.models.DayForecastWithHours
import com.weather.bratislava.data.models.ForecastItem
import com.weather.bratislava.data.models.HourlyForecast
import com.weather.bratislava.data.models.WeatherEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

object ForecastUtils {

    /**
     * Convert m/s to km/h
     */
    fun metersPerSecondToKmPerHour(mps: Double): Double {
        return mps * 3.6
    }

    /**
     * Convert Unix timestamp to readable date string
     */
    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val calendar = Calendar.getInstance().apply { time = date }
        val today = Calendar.getInstance()
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
        val dayAfterTomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 2) }

        return when {
            calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Today"

            calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR) -> "Tomorrow"

            calendar.get(Calendar.YEAR) == dayAfterTomorrow.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == dayAfterTomorrow.get(Calendar.DAY_OF_YEAR) -> "Day After Tomorrow"

            else -> {
                val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
                formatter.format(date)
            }
        }
    }

    /**
     * Parse raw forecast items and group by day
     * Returns top 3 days with current/main forecast for each day
     */
    fun parseForecasts(items: List<ForecastItem>): List<DayForecast> {
        val dateGroups = items.groupBy { formatDate(it.dt) }

        return dateGroups.entries.take(3).map { (dateLabel, dayItems) ->
            // Get the first item of the day (or the one closest to noon)
            val selectedItem = dayItems.firstOrNull() ?: return@map null

            DayForecast(
                date = dateLabel,
                temperature = selectedItem.main.temp.roundToInt().toDouble(),
                windSpeed = metersPerSecondToKmPerHour(selectedItem.wind.speed).roundToInt().toDouble(),
                rainProbability = (selectedItem.pop * 100).roundToInt(),
                weatherDescription = selectedItem.weather.firstOrNull()?.description ?: "Unknown",
                weatherIcon = selectedItem.weather.firstOrNull()?.icon ?: "01d"
            )
        }.filterNotNull()
    }

    /**
     * Convert DayForecast to WeatherEntity for database storage
     */
    fun toWeatherEntity(forecast: DayForecast): WeatherEntity {
        return WeatherEntity(
            date = forecast.date,
            temperature = forecast.temperature,
            windSpeed = forecast.windSpeed,
            rainProbability = forecast.rainProbability,
            weatherDescription = forecast.weatherDescription,
            weatherIcon = forecast.weatherIcon,
            timestamp = System.currentTimeMillis()
        )
    }

    /**
     * Convert WeatherEntity back to DayForecast
     */
    fun toDayForecast(entity: WeatherEntity): DayForecast {
        return DayForecast(
            date = entity.date,
            temperature = entity.temperature,
            windSpeed = entity.windSpeed,
            rainProbability = entity.rainProbability,
            weatherDescription = entity.weatherDescription,
            weatherIcon = entity.weatherIcon
        )
    }

    /**
     * Format time from Unix timestamp to HH:mm
     */
    fun formatTime(timestamp: Long): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp * 1000))
    }

    /**
     * Extract hour from Unix timestamp (0-23)
     */
    fun getHourFromTimestamp(timestamp: Long): Int {
        val calendar = Calendar.getInstance().apply { time = Date(timestamp * 1000) }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    /**
     * Parse forecasts with hourly details
     * Returns top 3 days, each with hourly forecasts for that day
     */
    fun parseForecasterWithHours(items: List<ForecastItem>): List<DayForecastWithHours> {
        val dateGroups = items.groupBy { formatDate(it.dt) }

        return dateGroups.entries.take(3).map { (dateLabel, dayItems) ->
            val selectedItem = dayItems.firstOrNull() ?: return@map null

            // Extract hourly forecasts (take next 8 hours for this day)
            val hourlyForecasts = dayItems.take(8).map { item ->
                HourlyForecast(
                    time = formatTime(item.dt),
                    hour = getHourFromTimestamp(item.dt),
                    temperature = item.main.temp.roundToInt().toDouble(),
                    rainProbability = (item.pop * 100).roundToInt(),
                    weatherIcon = item.weather.firstOrNull()?.icon ?: "01d"
                )
            }

            DayForecastWithHours(
                date = dateLabel,
                temperature = selectedItem.main.temp.roundToInt().toDouble(),
                windSpeed = metersPerSecondToKmPerHour(selectedItem.wind.speed).roundToInt().toDouble(),
                rainProbability = (selectedItem.pop * 100).roundToInt(),
                weatherDescription = selectedItem.weather.firstOrNull()?.description ?: "Unknown",
                weatherIcon = selectedItem.weather.firstOrNull()?.icon ?: "01d",
                hourlyForecasts = hourlyForecasts
            )
        }.filterNotNull()
    }
}
