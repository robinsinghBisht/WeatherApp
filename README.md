# WeatherApp

A Java Spring Boot application with a Vaadin UI to display current weather information for any city using the OpenWeatherMap API.

## Features
- Enter a city name to fetch and display current weather.
- Switch between Celsius (°C) and Fahrenheit (°F).
- Shows temperature, min/max, humidity, pressure, wind, and weather description.
- Modern web UI built with Vaadin.

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- Internet connection (for fetching weather data)
- OpenWeatherMap API key (see below)

## Getting an OpenWeatherMap API Key
1. Go to [https://home.openweathermap.org/users/sign_up](https://home.openweathermap.org/users/sign_up) and create a free account.
2. After verifying your email, log in and navigate to the "API keys" section in your account.
3. Copy your default key or generate a new one.

## Getting Started

### 1. Clone the repository
```bash
git clone <repo-url>
cd WeatherApp
```

### 2. Build the project
```bash
./mvnw clean install
```

### 3. Set your OpenWeatherMap API key as an environment variable

On Linux/macOS:
```bash
export key=YOUR_API_KEY
```
On Windows (Command Prompt):
```cmd
set key=YOUR_API_KEY
```
On Windows (PowerShell):
```powershell
$env:key="YOUR_API_KEY"
```

### 4. Run the application
```bash
./mvnw spring-boot:run
```

The application will start on [http://localhost:9090](http://localhost:9090) by default.

## Usage
- Open your browser and go to [http://localhost:9090](http://localhost:9090)
- Enter a city name in the input field.
- Select the temperature unit (°C or °F).
- Click the search button to view the current weather.

## Configuration
- The server port can be changed in `src/main/resources/application.properties` (default: `server.port=9090`).
- The OpenWeatherMap API key is read from the environment variable `key` and injected into the application at runtime.

## Technologies Used
- Java 17
- Spring Boot 2.7
- Vaadin 8
- OkHttp (HTTP client)
- org.json (JSON parsing)
- Maven

## License
This project is for demonstration purposes. 