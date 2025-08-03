import axios from 'axios';

const API_BASE_URL = 'http://localhost:9090/api/weather';

export interface WeatherData {
  city: string;
  temperature: number;
  feelsLike: number;
  tempMin: number;
  tempMax: number;
  pressure: number;
  humidity: number;
  windSpeed: number;
  windDirection: string;
  description: string;
  icon: string;
  unit: string; // Add unit information
}

export interface WeatherResponse {
  city: string;
  main: {
    temp: number;
    feels_like: number;
    temp_min: number;
    temp_max: number;
    pressure: number;
    humidity: number;
  };
  wind: {
    speed: number;
    deg: number;
  };
  weather: Array<{
    description: string;
    icon: string;
  }>;
}

export const weatherApi = {
  async getCurrentWeather(city: string, unit: string = 'metric'): Promise<WeatherResponse> {
    try {
      const response = await axios.get(`${API_BASE_URL}/current`, {
        params: { city, unit }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching weather data:', error);
      throw error;
    }
  },

  async getWeatherDetails(city: string, unit: string = 'metric'): Promise<WeatherData> {
    try {
      const response = await axios.get(`${API_BASE_URL}/details`, {
        params: { city, unit }
      });
      // Add the unit information to the response
      return { ...response.data, unit };
    } catch (error) {
      console.error('Error fetching weather details:', error);
      throw error;
    }
  }
}; 