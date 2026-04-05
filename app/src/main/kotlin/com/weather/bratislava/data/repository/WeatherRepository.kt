package com.weather.bratislava.data.repository

import com.weather.bratislava.data.api.OpenWeatherMapClient
import com.weather.bratislava.data.local.WeatherDatabase
import com.weather.bratislava.data.models.DayForecast
import com.weather.bratislava.data.models.DayForecastWithHours
import com.weather.bratislava.data.models.Resource
import com.weather.bratislava.data.utils.ForecastUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(private val database: WeatherDatabase) {

    companion object {
        private const val CACHE_DURATION_MS = 1 * 60 * 60 * 1000  // 1 hour
        private const val BRATISLAVA_LAT = 50.0755
        private const val BRATISLAVA_LON = 14.4378
        private const val API_KEY = "46513f42f678805d2f8f5e3c26174c4e"  // Replace with actual key
    }

    fun getWeather(): Flow<Resource<List<DayForecastWithHours>>> = flow {
        try {
            emit(Resource.Loading())

            // Check if cache is still valid
            val lastTimestamp = database.weatherDao().getLatestTimestamp()
            val isCacheValid = lastTimestamp != null &&
                    (System.currentTimeMillis() - lastTimestamp) < CACHE_DURATION_MS

            if (isCacheValid) {
                // Return cached data
                val cachedForecasts = database.weatherDao().getLatestThreeDays()
                cachedForecasts.collect { entities ->
                    val forecasts = entities.map { ForecastUtils.toDayForecast(it) }
                    // Create DayForecastWithHours with empty hourly list for cached data
                    val forecastsWithHours = forecasts.map { forecast ->
                        DayForecastWithHours(
                            date = forecast.date,
                            temperature = forecast.temperature,
                            windSpeed = forecast.windSpeed,
                            rainProbability = forecast.rainProbability,
                            weatherDescription = forecast.weatherDescription,
                            weatherIcon = forecast.weatherIcon,
                            hourlyForecasts = emptyList()
                        )
                    }
                    emit(Resource.Success(forecastsWithHours))
                }
            } else {
                // Fetch from API
                val response = OpenWeatherMapClient.weatherApi.getForecast(
                    latitude = BRATISLAVA_LAT,
                    longitude = BRATISLAVA_LON,
                    apiKey = API_KEY,
                    units = "metric"
                )

                // Parse response with hourly details
                val forecasts = ForecastUtils.parseForecasterWithHours(response.list)

                // Save daily data to database
                val entities = forecasts.map {
                    ForecastUtils.toWeatherEntity(
                        DayForecast(
                            date = it.date,
                            temperature = it.temperature,
                            windSpeed = it.windSpeed,
                            rainProbability = it.rainProbability,
                            weatherDescription = it.weatherDescription,
                            weatherIcon = it.weatherIcon
                        )
                    )
                }
                database.weatherDao().deleteAll()
                database.weatherDao().insertAll(entities)

                emit(Resource.Success(forecasts))
            }

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun refreshWeather(): Flow<Resource<List<DayForecastWithHours>>> = flow {
        try {
            emit(Resource.Loading())

            // Force API call, ignore cache
            val response = OpenWeatherMapClient.weatherApi.getForecast(
                latitude = BRATISLAVA_LAT,
                longitude = BRATISLAVA_LON,
                apiKey = API_KEY,
                units = "metric"
            )

            val forecasts = ForecastUtils.parseForecasterWithHours(response.list)

            // Update database
            val entities = forecasts.map {
                ForecastUtils.toWeatherEntity(
                    DayForecast(
                        date = it.date,
                        temperature = it.temperature,
                        windSpeed = it.windSpeed,
                        rainProbability = it.rainProbability,
                        weatherDescription = it.weatherDescription,
                        weatherIcon = it.weatherIcon
                    )
                )
            }
            database.weatherDao().deleteAll()
            database.weatherDao().insertAll(entities)

            emit(Resource.Success(forecasts))

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }
}
