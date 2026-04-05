# Setup Guide - Bratislava Weather App

Follow these steps to get the Bratislava Weather App running on your Android device or emulator.

## Prerequisites

- Android Studio (latest version recommended)
- Android SDK 24+ (minimum target API)
- JDK 11+
- OpenWeatherMap API key (free)

## Step 1: Create OpenWeatherMap Account & Get API Key

### Option A: Quick Setup (Recommended)

1. Go to **https://openweathermap.org/api**
2. Click **Sign Up** button
3. Fill in the registration form and create an account
4. Check your email and verify the account
5. Log in to your OpenWeatherMap account
6. Go to **API Keys** section (usually in your profile or settings)
7. Copy the **default API key** (or create a new one if you prefer)
8. **Save the API key** - you'll need it in the next step

### Option B: Using Forecast API Directly

If the above doesn't work:

1. Go to **https://openweathermap.org/api/forecast5**
2. Click **Subscribe** to the free tier
3. Complete the registration
4. Check your email and verify
5. Go to **API Keys** and copy your key

## Step 2: Configure API Key in the App

### Method 1: Edit Source Code (Simple)

1. Open `src/main/kotlin/com/weather/bratislava/data/repository/WeatherRepository.kt`
2. Find this line:
   ```kotlin
   private const val API_KEY = "YOUR_API_KEY_HERE"
   ```
3. Replace `YOUR_API_KEY_HERE` with your actual API key:
   ```kotlin
   private const val API_KEY = "abcd1234efgh5678ijkl9012mnop"  // Example key
   ```
4. Save the file (Ctrl+S or Cmd+S)

### Method 2: Using local.properties (Secure)

1. Create or edit `local.properties` file in the project root
2. Add this line:
   ```properties
   openweather.api.key=YOUR_API_KEY_HERE
   ```
3. Replace `YOUR_API_KEY_HERE` with your actual key
4. In `WeatherRepository.kt`, change the API_KEY constant to read from properties:
   ```kotlin
   // This requires additional setup in build.gradle.kts
   ```

## Step 3: Build and Run the Project

### Using Android Studio (Recommended)

1. **Open the project**:
   - File → Open → Navigate to `weather-app` folder
   - Click Open

2. **Sync Gradle** (if not automatic):
   - File → Sync Now
   - Wait for gradle to finish syncing

3. **Run the app**:
   - Click the **Run** button (green play icon) in the toolbar
   - Or use keyboard shortcut: **Shift + F10** (Windows/Linux) or **Control + R** (Mac)

4. **Select target**:
   - Choose Android Emulator or connected Physical Device
   - Click OK

### Using Command Line

```bash
# Navigate to project directory
cd /path/to/weather-app

# Build the project
./gradlew build

# Install on emulator or device
./gradlew installDebug

# Or use shortcut to build and run
./gradlew runDebug
```

## Step 4: Verify Installation

After the app installs and launches, you should see:

1. **App opens** with "Bratislava" as the title
2. **Loading spinner** appears for 2-3 seconds
3. **Weather cards** display showing:
   - Today's forecast
   - Tomorrow's forecast
   - Day after tomorrow's forecast
4. **Each card shows**:
   - Date (Today, Tomorrow, etc.)
   - Temperature in °C
   - Wind speed in km/h
   - Rain probability in %
   - Weather description

## Troubleshooting

### Problem: "Unable to load weather data" Error

**Cause 1**: Invalid or missing API key
- **Solution**: Double-check your API key in `WeatherRepository.kt`
- Make sure you copied it exactly without extra spaces

**Cause 2**: API key not activated
- **Solution**:
  - Go to your OpenWeatherMap account
  - Check if the API key status is "Active"
  - Wait a few minutes after creating a new key (sometimes takes time to activate)

**Cause 3**: Network connectivity
- **Solution**:
  - Check your internet connection
  - If using emulator, make sure it has internet access
  - Try disabling and re-enabling network in emulator

### Problem: Project won't build / "Gradle sync failed"

**Solution**:
1. Delete `.gradle` folder and `build` folder
2. In Android Studio: File → Invalidate Caches / Restart
3. Try syncing again: File → Sync Now

### Problem: "Minimum SDK must be 24 or higher"

**Solution**:
- The app requires Android 7.0 (API 24) or higher
- Update your Android SDK or use a newer emulator image

### Problem: App crashes immediately

**Cause**: Usually a build or runtime error
- **Solution**:
  1. Check Logcat (View → Tool Windows → Logcat)
  2. Look for error messages in red
  3. Common fixes:
     - Clean build: ./gradlew clean build
     - Invalidate cache: File → Invalidate Caches / Restart
     - Update dependencies if needed

### Problem: Blank white screen after launch

**Cause**: Usually a Compose rendering issue
- **Solution**:
  1. Ensure your device is Android 7.0+
  2. Check that API key is set correctly
  3. Look at Logcat for exceptions
  4. Try rebuilding: ./gradlew clean build

## Testing the App

Once installed, try these actions:

1. **Initial Load**: App should load weather data automatically
2. **Refresh**: Click the refresh icon (⟳) in top-right
3. **Offline Mode**: Disconnect internet, close and reopen app
   - Should show cached data from previous load
4. **Error Handling**:
   - Disable internet
   - Click refresh
   - Should show error message with retry button

## Configuration Options

### Change Location

To monitor a different city instead of Bratislava:

1. Open `WeatherRepository.kt`
2. Find these constants:
   ```kotlin
   private const val BRATISLAVA_LAT = 50.0755
   private const val BRATISLAVA_LON = 14.4378
   ```
3. Replace with your city's coordinates:
   ```kotlin
   private const val BRATISLAVA_LAT = YOUR_LAT  // e.g., 48.8566 for Paris
   private const val BRATISLAVA_LON = YOUR_LON  // e.g., 2.3522 for Paris
   ```
4. Rebuild and run

### Change Cache Duration

Default cache time is 1 hour. To modify:

1. Open `WeatherRepository.kt`
2. Find:
   ```kotlin
   private const val CACHE_DURATION_MS = 1 * 60 * 60 * 1000  // 1 hour
   ```
3. Change to your preferred duration (in milliseconds):
   ```kotlin
   private const val CACHE_DURATION_MS = 30 * 60 * 1000  // 30 minutes
   ```

## Next Steps

- **Customize UI**: Edit colors in `res/values/colors.xml`
- **Change themes**: Edit `res/values/themes.xml`
- **Add features**: Expand the app with additional weather data
- **Deploy**: Build release APK and publish to Play Store

## Resources

- [Android Documentation](https://developer.android.com/)
- [OpenWeatherMap API Docs](https://openweathermap.org/api)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Room Database Docs](https://developer.android.com/training/data-storage/room)
- [Retrofit Docs](https://square.github.io/retrofit/)

## Getting Help

If you encounter issues:

1. Check the [Troubleshooting](#troubleshooting) section
2. Look for error messages in Android Studio's Logcat
3. Search the error message on Stack Overflow
4. Check official documentation links above

Good luck! 🌤️
