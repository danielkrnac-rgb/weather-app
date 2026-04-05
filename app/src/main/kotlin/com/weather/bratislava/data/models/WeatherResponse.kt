package com.weather.bratislava.data.models

import com.google.gson.annotations.SerializedName

// Main API response structure from OpenWeatherMap
data class WeatherResponse(
    val list: List<ForecastItem>,
    val city: City
)

data class City(
    val name: String,
    val coord: Coordinates
)

data class Coordinates(
    val lat: Double,
    val lon: Double
)

data class ForecastItem(
    val dt: Long,  // Unix timestamp
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val clouds: Clouds,
    val sys: Sys,
    val pop: Double  // Probability of precipitation (0-1)
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double,  // m/s
    val deg: Int,
    val gust: Double? = null
)

data class Clouds(
    val all: Int  // Cloudiness percentage
)

data class Sys(
    val pod: String  // "d" for day, "n" for night
)
