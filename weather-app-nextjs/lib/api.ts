import axios from 'axios';

// In production the Next.js server rewrites this same-origin path to the private
// Spring container. Override it only when the backend is hosted separately.
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || '/api/weather';

export type TemperatureUnit = 'metric' | 'imperial';

export interface WeatherData {
  city: string;
  country: string;
  temperature: number;
  feelsLike: number;
  tempMin: number;
  tempMax: number;
  pressure: number;
  humidity: number;
  windSpeed: number;
  windDegree: number;
  windDirection: string;
  visibility: number;
  clouds: number;
  description: string;
  icon: string;
  sunrise: number;
  sunset: number;
  observedAt: number;
  timezone: number;
  lat: number;
  lon: number;
  unit: TemperatureUnit;
}

export interface ForecastItem {
  timestamp: number;
  temperature: number;
  feelsLike: number;
  humidity: number;
  windSpeed: number;
  rainChance: number;
  rainVolume: number;
  description: string;
  icon: string;
}

export interface WeatherOverview {
  current: WeatherData;
  forecast: ForecastItem[];
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
  async getOverview(city: string, unit: TemperatureUnit = 'metric'): Promise<WeatherOverview> {
    const response = await axios.get<WeatherOverview>(`${API_BASE_URL}/overview`, {
      params: { city, unit },
    });
    return response.data;
  },

  async getCurrentWeather(city: string, unit: TemperatureUnit = 'metric'): Promise<WeatherResponse> {
    const response = await axios.get<WeatherResponse>(`${API_BASE_URL}/current`, {
      params: { city, unit },
    });
    return response.data;
  },

  async getWeatherDetails(city: string, unit: TemperatureUnit = 'metric'): Promise<WeatherData> {
    const response = await axios.get<WeatherData>(`${API_BASE_URL}/details`, {
      params: { city, unit },
    });
    return response.data;
  },
};
