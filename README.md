# THIS IS AN ENTIRELY AI GENERATED CODE PACKAGE, USE WITH CAUTION

# Provide a openweathermap API Key in application.properties before running

# Style Recommendation Service
This application provides style recommendations based on current weather conditions for a given location.
Features

Fetches real-time weather data
Provides style recommendations for men and women
Offers reasoning behind the recommendations

Setup
Prerequisites

Java 17 or higher
Node.js and npm
Gradle

Backend

Navigate to the backend directory:
```
cd backend
```
Build the Spring Boot application:
```
./gradlew build
```
Run the application:
```
./gradlew bootRun
```

Frontend

Navigate to the frontend directory:
```
cd frontend
```
Install dependencies:
```
npm install
```
Start the React application:
```
npm start
```

Usage
Open a web browser and navigate to http://localhost:3000. Enter a location to receive style recommendations based on the current weather.
Technologies Used

Backend: Spring Boot
Frontend: React
Weather Data: OpenWeatherMap API

Configuration
To use this application, you need to set up an OpenWeatherMap API key:

Sign up for a free API key at OpenWeatherMap
Create a file `application.properties` in `backend/src/main/resources/` if it doesn't exist
Add the following line to the file, replacing `YOUR_API_KEY` with your actual API key:
```
openweathermap.api.key=YOUR_API_KEY
```

Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
License
MIT
