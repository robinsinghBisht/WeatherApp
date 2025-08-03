'use client';

import { useState } from 'react';
import SearchForm from '@/components/SearchForm';
import WeatherCard from '@/components/WeatherCard';
import { weatherApi, WeatherData } from '@/lib/api';
import { AlertCircle } from 'lucide-react';

export default function Home() {
  const [weather, setWeather] = useState<WeatherData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSearch = async (city: string, unit: string) => {
    setLoading(true);
    setError(null);
    
    try {
      const weatherData = await weatherApi.getWeatherDetails(city, unit);
      setWeather(weatherData);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to fetch weather data. Please try again.');
      setWeather(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-100 to-purple-100 py-8 px-4">
      <div className="max-w-4xl mx-auto">
        <SearchForm onSearch={handleSearch} loading={loading} />
        
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6 max-w-md mx-auto">
            <div className="flex items-center">
              <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
              <span className="text-red-800">{error}</span>
            </div>
          </div>
        )}
        
        {weather && <WeatherCard weather={weather} />}
        
        {!weather && !loading && !error && (
          <div className="text-center text-gray-600 mt-8">
            <p>Enter a city name to get weather information</p>
          </div>
        )}
      </div>
    </div>
  );
} 