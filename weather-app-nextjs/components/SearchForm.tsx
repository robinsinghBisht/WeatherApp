'use client';

import { useState } from 'react';
import { Search, ChevronDown, MapPin } from 'lucide-react';

interface SearchFormProps {
  onSearch: (city: string, unit: string) => void;
  loading: boolean;
}

export default function SearchForm({ onSearch, loading }: SearchFormProps) {
  const [city, setCity] = useState('');
  const [unit, setUnit] = useState('metric');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (city.trim()) {
      onSearch(city.trim(), unit);
    }
  };

  const getUnitDisplayText = (unitValue: string) => {
    return unitValue === 'metric' ? 'Celsius (°C)' : 'Fahrenheit (°F)';
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-6 max-w-md mx-auto mb-8">
      <h1 className="text-2xl font-bold text-center text-gray-800 mb-6">
        Weather App
      </h1>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="city" className="block text-sm font-medium text-gray-700 mb-2">
            City Name
          </label>
          <div className="relative">
            <input
              type="text"
              id="city"
              value={city}
              onChange={(e) => setCity(e.target.value)}
              placeholder="Enter city name..."
              className="w-full px-4 py-2 pl-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-gray-900 bg-white"
              required
              autoComplete="off"
            />
            <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
              <MapPin className="w-4 h-4 text-gray-400" />
            </div>
          </div>
          {city && (
            <div className="mt-1 text-sm text-gray-500">
              Typed: {city}
            </div>
          )}
        </div>

        <div>
          <label htmlFor="unit" className="block text-sm font-medium text-gray-700 mb-2">
            Temperature Unit
          </label>
          <div className="relative">
            <select
              id="unit"
              value={unit}
              onChange={(e) => setUnit(e.target.value)}
              className="w-full px-4 py-2 pr-10 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent appearance-none bg-white cursor-pointer text-gray-900"
              style={{ backgroundImage: 'none' }}
            >
              <option value="metric">Celsius (°C)</option>
              <option value="imperial">Fahrenheit (°F)</option>
            </select>
            <div className="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none">
              <ChevronDown className="w-4 h-4 text-gray-400" />
            </div>
          </div>
          <div className="mt-1 text-sm text-gray-500">
            Selected: {getUnitDisplayText(unit)}
          </div>
        </div>

        <button
          type="submit"
          disabled={loading || !city.trim()}
          className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center justify-center"
        >
          {loading ? (
            <div className="flex items-center">
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
              Loading...
            </div>
          ) : (
            <>
              <Search className="w-4 h-4 mr-2" />
              Get Weather
            </>
          )}
        </button>
      </form>
    </div>
  );
} 