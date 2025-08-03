'use client';

import { WeatherData } from '@/lib/api';
import { Cloud, Thermometer, Wind, Droplets, Gauge } from 'lucide-react';

interface WeatherCardProps {
  weather: WeatherData;
}

export default function WeatherCard({ weather }: WeatherCardProps) {
  const getWeatherIcon = (iconCode: string) => {
    return `http://openweathermap.org/img/wn/${iconCode}@2x.png`;
  };

  const getTemperatureUnit = () => {
    return weather.unit === 'imperial' ? 'F' : 'C';
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-6 max-w-md mx-auto">
      {/* Header */}
      <div className="text-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800">{weather.city}</h2>
        <div className="flex items-center justify-center mt-2">
          <img 
            src={getWeatherIcon(weather.icon)} 
            alt={weather.description}
            className="w-16 h-16"
          />
          <div className="ml-4">
            <p className="text-3xl font-bold text-gray-800">
              {Math.round(weather.temperature)}°{getTemperatureUnit()}
            </p>
            <p className="text-gray-600 capitalize">{weather.description}</p>
          </div>
        </div>
      </div>

      {/* Weather Details */}
      <div className="space-y-4">
        {/* Temperature Range */}
        <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
          <div className="flex items-center">
            <Thermometer className="w-5 h-5 text-blue-600 mr-2" />
            <span className="text-gray-700">Temperature Range</span>
          </div>
          <span className="font-semibold text-gray-800">
            {Math.round(weather.tempMin)}°{getTemperatureUnit()} - {Math.round(weather.tempMax)}°{getTemperatureUnit()}
          </span>
        </div>

        {/* Feels Like */}
        <div className="flex items-center justify-between p-3 bg-green-50 rounded-lg">
          <div className="flex items-center">
            <Cloud className="w-5 h-5 text-green-600 mr-2" />
            <span className="text-gray-700">Feels Like</span>
          </div>
          <span className="font-semibold text-gray-800">
            {Math.round(weather.feelsLike)}°{getTemperatureUnit()}
          </span>
        </div>

        {/* Wind */}
        <div className="flex items-center justify-between p-3 bg-yellow-50 rounded-lg">
          <div className="flex items-center">
            <Wind className="w-5 h-5 text-yellow-600 mr-2" />
            <span className="text-gray-700">Wind</span>
          </div>
          <span className="font-semibold text-gray-800">
            {weather.windSpeed} m/s {weather.windDirection}
          </span>
        </div>

        {/* Humidity */}
        <div className="flex items-center justify-between p-3 bg-purple-50 rounded-lg">
          <div className="flex items-center">
            <Droplets className="w-5 h-5 text-purple-600 mr-2" />
            <span className="text-gray-700">Humidity</span>
          </div>
          <span className="font-semibold text-gray-800">
            {weather.humidity}%
          </span>
        </div>

        {/* Pressure */}
        <div className="flex items-center justify-between p-3 bg-red-50 rounded-lg">
          <div className="flex items-center">
            <Gauge className="w-5 h-5 text-red-600 mr-2" />
            <span className="text-gray-700">Pressure</span>
          </div>
          <span className="font-semibold text-gray-800">
            {weather.pressure} hPa
          </span>
        </div>
      </div>
    </div>
  );
} 