package com.robin.weather.controller;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
	 private OkHttpClient client;
	 private Response response;
	 private String cityName="New Delhi";
	 String unit="metric";
	 @SuppressWarnings("unused")
	private String API="e081f8e2fca5d5f3b840cb5239637a73";
	 
	 public JSONObject getWeather()
	 {
		 client=new OkHttpClient();
		 Request request=new Request.Builder().
				 url("http://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+"&appid=e081f8e2fca5d5f3b840cb5239637a73")
				 .build();
		 try {
		 response=client.newCall(request).execute();
		 return new JSONObject(response.body().string());
		 }
		 catch(IOException e)
		 {
			 e.printStackTrace();
		 }
		 return null;
	 }
	 
	 public JSONArray returnWeatherArray()
	 { 
		 JSONArray weatherArray=getWeather().getJSONArray("weather");
		 return weatherArray;
		 
	 }
	 
	 public JSONObject returnMain() throws JSONException
	 {
		JSONObject main=getWeather().getJSONObject("main");
		return main;
	 }
	 public JSONObject returnWind() throws JSONException
	 {
		JSONObject wind=getWeather().getJSONObject("wind");
		return wind;
	 }
	 public JSONObject returnSys() throws JSONException
	 {
		JSONObject sys=getWeather().getJSONObject("sys"	);
		return sys;
	 }
	 public String returnName() throws JSONException
	 {
		String name=getWeather().getString("name");
		return name;
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
		/*public static void main(String str[])
		{
			
			System.out.println(new MyWeather().returnName());
		}*/

	public String convertDegreeToCardinalDirection(int directionInDegrees){
		String cardinalDirection = null;
		if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
				(directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
			cardinalDirection = "N";
		} else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
			cardinalDirection = "NNE";
		} else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
			cardinalDirection = "NE";
		} else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
			cardinalDirection = "ENE";
		} else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
			cardinalDirection = "E";
		} else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
			cardinalDirection = "ESE";
		} else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
			cardinalDirection = "SE";
		} else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
			cardinalDirection = "SSE";
		} else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
			cardinalDirection = "S";
		} else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
			cardinalDirection = "SSW";
		} else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
			cardinalDirection = "SW";
		} else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
			cardinalDirection = "WSW";
		} else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
			cardinalDirection = "W";
		} else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
			cardinalDirection = "WNW";
		} else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
			cardinalDirection = "NW";
		} else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
			cardinalDirection = "NNW";
		} else {
			cardinalDirection = "?";
		}

		return cardinalDirection;
	}

}
