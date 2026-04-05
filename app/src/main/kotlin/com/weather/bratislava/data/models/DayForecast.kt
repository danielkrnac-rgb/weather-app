package com.weather.bratislava.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.bratislava.data.models.DayForecastWithHours

// Parsed day forecast for UI display
data class DayForecast(
    val date: String,  // "Today", "Tomorrow", or date string
    val temperature: Double,  // in Celsius
    val windSpeed: Double,  // in km/h
    val rainProbability: Int,  // 0-100 percentage
    val weatherDescription: String,
    val weatherIcon: String
)

// Room database entity for caching
@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val temperature: Double,
    val windSpeed: Double,
    val rainProbability: Int,
    val weatherDescription: String,
    val weatherIcon: String,
    val timestamp: Long  // When this was cached
)

// Wrapper for API response with state
sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}

// UI state for ViewModel
data class WeatherUiState(
    val isLoading: Boolean = false,
    val forecasts: List<DayForecastWithHours> = emptyList(),
    val error: String? = null
)
