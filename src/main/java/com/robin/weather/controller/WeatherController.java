package com.robin.weather.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentWeather(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String unit) {
        try {
            return ResponseEntity.ok(toCurrentResponse(weatherService.fetchCurrent(city, unit), unit));
        } catch (Exception e) {
            return error(e);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getWeatherDetails(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String unit) {
        try {
            return ResponseEntity.ok(toDetailsResponse(weatherService.fetchCurrent(city, unit), unit));
        } catch (Exception e) {
            return error(e);
        }
    }

    @GetMapping("/overview")
    public ResponseEntity<?> getWeatherOverview(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String unit) {
        try {
            JSONObject current = weatherService.fetchCurrent(city, unit);
            JSONObject forecast = weatherService.fetchForecast(city, unit);

            Map<String, Object> response = new HashMap<>();
            response.put("current", toDetailsResponse(current, unit));
            response.put("forecast", toForecastResponse(forecast));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return error(e);
        }
    }

    private Map<String, Object> toCurrentResponse(JSONObject weather, String unit) {
        Map<String, Object> response = new HashMap<>();
        response.put("city", weather.optString("name"));
        response.put("country", object(weather, "sys").optString("country"));
        response.put("main", object(weather, "main").toMap());
        response.put("wind", object(weather, "wind").toMap());
        response.put("weather", array(weather, "weather").toList());
        response.put("unit", unit);
        return response;
    }

    private Map<String, Object> toDetailsResponse(JSONObject weather, String unit) {
        JSONObject main = object(weather, "main");
        JSONObject wind = object(weather, "wind");
        JSONObject sys = object(weather, "sys");
        JSONObject weatherInfo = array(weather, "weather").optJSONObject(0);
        if (weatherInfo == null) {
            weatherInfo = new JSONObject();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("city", weather.optString("name"));
        response.put("country", sys.optString("country"));
        response.put("temperature", main.optDouble("temp"));
        response.put("feelsLike", main.optDouble("feels_like"));
        response.put("tempMin", main.optDouble("temp_min"));
        response.put("tempMax", main.optDouble("temp_max"));
        response.put("pressure", main.optInt("pressure"));
        response.put("humidity", main.optInt("humidity"));
        response.put("windSpeed", wind.optDouble("speed"));
        response.put("windDegree", wind.optInt("deg"));
        response.put("windDirection", weatherService.convertDegreeToCardinalDirection(wind.optInt("deg")));
        response.put("visibility", weather.optInt("visibility", 0));
        response.put("clouds", object(weather, "clouds").optInt("all", 0));
        response.put("description", weatherInfo.optString("description", "No description"));
        response.put("icon", weatherInfo.optString("icon", "01d"));
        response.put("sunrise", sys.optLong("sunrise", 0));
        response.put("sunset", sys.optLong("sunset", 0));
        response.put("observedAt", weather.optLong("dt", 0));
        response.put("timezone", weather.optInt("timezone", 0));
        response.put("lat", object(weather, "coord").optDouble("lat", 0));
        response.put("lon", object(weather, "coord").optDouble("lon", 0));
        response.put("unit", unit);
        return response;
    }

    private java.util.List<Map<String, Object>> toForecastResponse(JSONObject forecast) {
        java.util.List<Map<String, Object>> items = new java.util.ArrayList<>();
        JSONArray list = array(forecast, "list");
        for (int i = 0; i < Math.min(list.length(), 8); i++) {
            JSONObject item = list.optJSONObject(i);
            if (item == null) continue;
            JSONObject main = object(item, "main");
            JSONObject wind = object(item, "wind");
            JSONObject rain = object(item, "rain");
            JSONObject info = array(item, "weather").optJSONObject(0);
            if (info == null) info = new JSONObject();

            Map<String, Object> mapped = new HashMap<>();
            mapped.put("timestamp", item.optLong("dt", 0));
            mapped.put("temperature", main.optDouble("temp"));
            mapped.put("feelsLike", main.optDouble("feels_like"));
            mapped.put("humidity", main.optInt("humidity"));
            mapped.put("windSpeed", wind.optDouble("speed"));
            mapped.put("rainChance", item.optDouble("pop", 0) * 100);
            mapped.put("rainVolume", rain.optDouble("3h", 0));
            mapped.put("description", info.optString("description", "No description"));
            mapped.put("icon", info.optString("icon", "01d"));
            items.add(mapped);
        }
        return items;
    }

    private JSONObject object(JSONObject parent, String key) {
        JSONObject value = parent.optJSONObject(key);
        return value == null ? new JSONObject() : value;
    }

    private JSONArray array(JSONObject parent, String key) {
        JSONArray value = parent.optJSONArray(key);
        return value == null ? new JSONArray() : value;
    }

    private ResponseEntity<Map<String, String>> error(Exception exception) {
        Map<String, String> body = new HashMap<>();
        body.put("error", exception.getMessage() == null ? "Failed to fetch weather data" : exception.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}
