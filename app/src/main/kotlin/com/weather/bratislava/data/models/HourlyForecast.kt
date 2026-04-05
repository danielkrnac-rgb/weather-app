package com.weather.bratislava.data.models

// Hourly forecast for next 24-48 hours
data class HourlyForecast(
    val time: String,  // "14:00", "15:00", etc.
    val hour: Int,  // 0-23
    val temperature: Double,  // in Celsius
    val rainProbability: Int,  // 0-100 percentage
    val weatherIcon: String
)

// Extended day forecast with hourly data
data class DayForecastWithHours(
    val date: String,  // "Today", "Tomorrow", etc.
    val temperature: Double,
    val windSpeed: Double,
    val rainProbability: Int,
    val weatherDescription: String,
    val weatherIcon: String,
    val hourlyForecasts: List<HourlyForecast>  // Next 8 hours for this day
)
