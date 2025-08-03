package com.robin.weather.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentWeather(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String unit) {
        
        try {
            weatherService.setCityName(city);
            weatherService.setUnit(unit);
            
            JSONObject weatherData = weatherService.getWeather();
            if (weatherData == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("city", weatherService.returnName());
            response.put("main", weatherService.returnMain().toMap());
            response.put("wind", weatherService.returnWind().toMap());
            response.put("weather", weatherService.returnWeatherArray().toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch weather data: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getWeatherDetails(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String unit) {
        
        try {
            weatherService.setCityName(city);
            weatherService.setUnit(unit);
            
            JSONObject weatherData = weatherService.getWeather();
            if (weatherData == null) {
                return ResponseEntity.notFound().build();
            }

            JSONObject main = weatherService.returnMain();
            JSONObject wind = weatherService.returnWind();
            JSONArray weatherArray = weatherService.returnWeatherArray();
            
            Map<String, Object> response = new HashMap<>();
            response.put("city", weatherService.returnName());
            response.put("temperature", main.getInt("temp"));
            response.put("feelsLike", main.getInt("feels_like"));
            response.put("tempMin", main.getInt("temp_min"));
            response.put("tempMax", main.getInt("temp_max"));
            response.put("pressure", main.getInt("pressure"));
            response.put("humidity", main.getInt("humidity"));
            response.put("windSpeed", wind.getInt("speed"));
            response.put("windDirection", weatherService.convertDegreeToCardinalDirection(wind.getInt("deg")));
            
            // Get weather description and icon
            if (weatherArray.length() > 0) {
                JSONObject weather = weatherArray.getJSONObject(0);
                response.put("description", weather.getString("description"));
                response.put("icon", weather.getString("icon"));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch weather details: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 