package com.robin.weather.controller;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {
    private static final String CURRENT_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast";

    private final OkHttpClient client = new OkHttpClient();

    @Value("${openweathermap.api.key:}")
    private String apiKey;

    // Kept for the original Vaadin view, while the REST API uses the stateless methods below.
    private String cityName = "New Delhi";
    private String unit = "metric";

    public JSONObject fetchCurrent(String city, String requestedUnit) throws IOException {
        return fetch(CURRENT_URL, city, requestedUnit);
    }

    public JSONObject fetchForecast(String city, String requestedUnit) throws IOException {
        return fetch(FORECAST_URL, city, requestedUnit);
    }

    private JSONObject fetch(String endpoint, String city, String requestedUnit) throws IOException {
        String safeUnit = "imperial".equalsIgnoreCase(requestedUnit) ? "imperial" : "metric";
        HttpUrl baseUrl = HttpUrl.parse(endpoint);
        if (baseUrl == null) {
            throw new IOException("Invalid OpenWeather endpoint");
        }

        HttpUrl url = baseUrl.newBuilder()
                .addQueryParameter("q", city.trim())
                .addQueryParameter("units", safeUnit)
                .addQueryParameter("appid", apiKey)
                .build();

        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            String body = response.body() == null ? "{}" : response.body().string();
            if (!response.isSuccessful()) {
                String message = new JSONObject(body).optString("message", "OpenWeather request failed");
                throw new IOException(message);
            }
            return new JSONObject(body);
        }
    }

    // Legacy methods used by the Vaadin screen.
    public JSONObject getWeather() {
        try {
            return fetchCurrent(cityName, unit);
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    public JSONArray returnWeatherArray() {
        JSONObject weather = getWeather();
        if (weather == null || weather.optJSONArray("weather") == null) {
            return new JSONArray();
        }
        return weather.optJSONArray("weather");
    }

    public JSONObject returnMain() throws JSONException {
        JSONObject weather = getWeather();
        if (weather == null) {
            throw new JSONException("Weather data is unavailable");
        }
        return weather.getJSONObject("main");
    }

    public JSONObject returnWind() throws JSONException {
        JSONObject weather = getWeather();
        if (weather == null) {
            throw new JSONException("Weather data is unavailable");
        }
        return weather.getJSONObject("wind");
    }

    public JSONObject returnSys() throws JSONException {
        JSONObject weather = getWeather();
        if (weather == null) {
            throw new JSONException("Weather data is unavailable");
        }
        return weather.getJSONObject("sys");
    }

    public String returnName() throws JSONException {
        JSONObject weather = getWeather();
        if (weather == null) {
            throw new JSONException("Weather data is unavailable");
        }
        return weather.getString("name");
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String convertDegreeToCardinalDirection(int directionInDegrees) {
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = (int) Math.round(((directionInDegrees % 360) / 22.5)) % 16;
        return directions[index];
    }
}
