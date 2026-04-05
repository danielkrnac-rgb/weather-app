# Bratislava Weather App

A minimalist Android weather application displaying current weather and 3-day forecast for Bratislava.

## Features

- **Current Weather**: Temperature (°C), wind speed (km/h), and rain probability
- **3-Day Forecast**: Extended forecast for today, tomorrow, and day after tomorrow
- **Offline Caching**: Caches weather data for 1 hour
- **Material Design 3**: Modern UI with Jetpack Compose
- **Kotlin**: Built entirely in Kotlin with coroutines and modern architecture

## Technologies Used

- **Kotlin**: Programming language
- **Jetpack Compose**: Modern UI toolkit
- **Retrofit**: API client
- **Room**: Local database for caching
- **Coroutines**: Asynchronous programming
- **MVVM Architecture**: Clean separation of concerns

## Setup Instructions

### 1. Get OpenWeatherMap API Key

1. Go to [OpenWeatherMap](https://openweathermap.org/api/forecast5)
2. Create a free account
3. Verify your email
4. Navigate to API keys section
5. Copy your default API key (or create a new one)

### 2. Configure API Key

Replace `YOUR_API_KEY_HERE` in `src/main/kotlin/com/weather/bratislava/data/repository/WeatherRepository.kt`:

```kotlin
private const val API_KEY = "YOUR_API_KEY_HERE"
```

### 3. Build and Run

```bash
# Build the project
./gradlew build

# Run on emulator or connected device
./gradlew installDebug

# Or open the project in Android Studio and click "Run"
```

## Project Structure

```
app/src/main/
├── kotlin/com/weather/bratislava/
│   ├── data/
│   │   ├── api/              # Retrofit API interfaces
│   │   ├── local/            # Room database
│   │   ├── models/           # Data classes
│   │   ├── repository/       # Repository pattern
│   │   └── utils/            # Utility functions
│   ├── ui/
│   │   ├── screens/          # Main screen
│   │   ├── components/       # Composable components
│   │   ├── viewmodel/        # ViewModel
│   │   └── MainActivity.kt    # Entry point
│   └── res/                  # Resources (strings, colors, themes)
```

## How It Works

1. **App Launch**: ViewModel loads weather data from repository
2. **Cache Check**: Repository checks if cached data is valid (< 1 hour)
3. **API Call**: If cache is invalid, fetches fresh data from OpenWeatherMap
4. **Parse & Store**: Parses response and caches in local database
5. **UI Update**: ViewModel state updates and Compose re-renders the UI

## Configuration

### Cache Duration

Default cache duration is 1 hour. To modify, edit `WeatherRepository.kt`:

```kotlin
private const val CACHE_DURATION_MS = 1 * 60 * 60 * 1000  // 1 hour
```

### Bratislava Coordinates

Latitude: `50.0755` N
Longitude: `14.4378` E

To change location, modify coordinates in `WeatherRepository.kt`:

```kotlin
private const val BRATISLAVA_LAT = 50.0755
private const val BRATISLAVA_LON = 14.4378
```

## API Response Structure

The app parses the 5-day forecast API from OpenWeatherMap:

- **Endpoint**: `https://api.openweathermap.org/data/2.5/forecast`
- **Forecast Items**: 40 items (5 days × 8 per day, every 3 hours)
- **Parsed Data**: Top 3 days with temperature, wind speed, and rain probability

## Troubleshooting

### "API Error: Unable to fetch weather data"
- Check your internet connection
- Verify API key is correctly set in `WeatherRepository.kt`
- Check if OpenWeatherMap API key is activated on their website

### App Crashes on Launch
- Ensure minimum SDK is 24 or higher
- Check that all dependencies are properly installed: `./gradlew clean build`

### Empty Forecast List
- Ensure the API response is being parsed correctly
- Check Logcat for API errors: `adb logcat | grep WeatherApi`

## Testing

### Manual Testing Steps

1. Open app - should load weather data
2. Click refresh icon - should fetch fresh data
3. Close app within 1 hour - should show cached data
4. Disable internet and try to refresh - should show error message
5. Re-enable internet and retry - should fetch fresh data

## Future Enhancements

- [ ] Multiple location support
- [ ] Weather alerts and notifications
- [ ] Detailed hourly forecast
- [ ] Weather icons/animations
- [ ] Dark mode improvements
- [ ] Unit conversion (Celsius ↔ Fahrenheit)
- [ ] Widgets

## License

This project is open source and available under the MIT License.

## Support

For questions or issues, check the Android documentation or OpenWeatherMap API docs.
