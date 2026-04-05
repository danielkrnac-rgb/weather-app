package com.weather.bratislava.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.bratislava.R
import com.weather.bratislava.data.models.DayForecastWithHours

@Composable
fun DayForecastCard(forecast: DayForecastWithHours) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Date header
            Text(
                text = forecast.date,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // Weather info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Temperature
                ForecastInfoItem(
                    label = stringResource(R.string.temperature),
                    value = "${forecast.temperature.toInt()}${stringResource(R.string.celsius)}",
                    modifier = Modifier.weight(1f)
                )

                // Wind Speed
                ForecastInfoItem(
                    label = stringResource(R.string.wind_speed),
                    value = "${forecast.windSpeed.toInt()} ${stringResource(R.string.kmh)}",
                    modifier = Modifier.weight(1f)
                )

                // Rain Probability
                ForecastInfoItem(
                    label = stringResource(R.string.rain_probability),
                    value = "${forecast.rainProbability}${stringResource(R.string.percent)}",
                    modifier = Modifier.weight(1f)
                )
            }

            // Weather description
            Text(
                text = forecast.weatherDescription.replaceFirstChar { it.uppercase() },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            // Hourly forecast if available
            if (forecast.hourlyForecasts.isNotEmpty()) {
                HourlyForecastRow(hourlyForecasts = forecast.hourlyForecasts)
            }
        }
    }
}

@Composable
private fun ForecastInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}
