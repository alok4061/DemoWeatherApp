# Open Weather App (1.0)

Weather SDK uses APIs provided by [openweathermap](https://openweathermap.org/) which provides
weather related data like rain, snow extreme etc.

## Installation

Installation is very straightforward, you need to add download the library
from [github](https://github.com/alok4061/DemoWeatherApp) and paste in the
same app folder in which you want to integrate it, then follow the steps below:

Or we can also make it gradle dependency using jitpack
https://jitpack.io/

Step 1: Follow to File > New Module
![](https://www.geeksforgeeks.org/how-to-add-a-library-project-to-android-studio/)

Click on “Import Existing Project“.
![](https://media.geeksforgeeks.org/wp-content/uploads/20210327124538/Zm7QO.png)

Step 2: Select the desired library and the desired module. Then click finish. Android Studio will
import the library into your project and will sync Gradle files.

Step 3: In the next step you need to add the imported module to your project’s dependencies.
Right-click on the app folder > Open Module settings

![](https://media.geeksforgeeks.org/wp-content/uploads/20210326232940/Screenshot414.png)

Step 4: Navigate to the dependencies tab > Click on the ‘+’ button -> click on Module Dependency.
The library module will be then added to the project’s dependencies.

![](https://media.geeksforgeeks.org/wp-content/uploads/20210326233244/Screenshot420.png)

it's done, your library is ready to be used!

## Usage 1 : Get Today,s weather data

        val weatherSDK = WeatherSDK.getInstance(getString(R.string.key)) // use your key generated from https://openweathermap.org/

then to fetch current location's weather forecast details

        weatherSDK.getCurrentWeatherToday(28.7041, 77.1025,
            WeatherSDK.TempUnit.CELSIUS,
            object : WeatherSDK.WeatherDataListener {
                override fun onWeatherResponse(response: WeatherResponse) {
                    Log.d(TAG, response.toString())
                }

                override fun onErrorFetchingData(error: Throwable) {
                    Log.d(TAG, error.message.toString())
                }
            })

## Usage 2 : Get weather data for week

               private fun getCurrentWeatherForWeek() {
                   weatherSDK.getCurrentWeatherforWeek(28.7041, 77.1025,
                       WeatherSDK.TempUnit.FAHRENHEIT,
                       exclude,
                       cnt,
                       object : WeatherSDK.WeatherDataListenerforWeek {
                           override fun onWeatherResponseforWeek(response: WeatherWeekResponse) {
                               Log.d(TAG, response.toString())
                           }

                           override fun onErrorFetchingData(error: Throwable) {
                               Log.d(TAG, error.message.toString())
                           }
                       })

               }
select radio button for celsius and Fahrenheit and click on button to get the Weather information
according to choice of Today and week

use WeatherSDK.TempUnit.CELSIUS to get temperature in celsius or WeatherSDK.TempUnit.FAHRENHEIT to
get temperature in Fahrenheit

cnt is number of days you want to check for weather report

# Unit test for celsius to fahrenheit and viceversa
# Unit test for validate lat,lng and weather response for same day and week

      
