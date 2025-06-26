package com.robin.weather;

//import okhttp3.Request.Builder;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyWeather
 {
	 private OkHttpClient client;
	 private Response response;
	 private String cityName="New Delhi";
	 String unit="metric";
	 @Value("${openweathermap.api.key}")
	 private String API;
	 
	 public JSONObject getWeather()
	 {
		 client=new OkHttpClient();
		 Request request=new Request.Builder().
				 url("http://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+"&appid="+API)
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

 }
