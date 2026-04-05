package com.weather.bratislava.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.bratislava.data.models.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(forecasts: List<WeatherEntity>)

    @Query("SELECT * FROM weather_cache ORDER BY date ASC")
    fun getAllForecasts(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather_cache ORDER BY date ASC LIMIT 3")
    fun getLatestThreeDays(): Flow<List<WeatherEntity>>

    @Query("DELETE FROM weather_cache")
    suspend fun deleteAll()

    @Query("SELECT MAX(timestamp) FROM weather_cache")
    suspend fun getLatestTimestamp(): Long?
}
