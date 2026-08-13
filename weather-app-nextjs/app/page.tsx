'use client';

import { useState } from 'react';
import { AlertCircle, Compass, Loader2 } from 'lucide-react';
import SearchForm from '@/components/SearchForm';
import WeatherCard from '@/components/WeatherCard';
import { TemperatureUnit, WeatherOverview, weatherApi } from '@/lib/api';

export default function Home() {
  const [overview, setOverview] = useState<WeatherOverview | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSearch = async (city: string, unit: TemperatureUnit) => {
    setLoading(true);
    setError(null);
    try {
      setOverview(await weatherApi.getOverview(city, unit));
    } catch (requestError: unknown) {
      const message = typeof requestError === 'object' && requestError !== null && 'response' in requestError
        ? ((requestError as { response?: { data?: { error?: string } } }).response?.data?.error)
        : undefined;
      setError(message || 'We could not find that location. Try a city name or nearby town.');
      setOverview(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-shell">
      <div className="ambient ambient-one" />
      <div className="ambient ambient-two" />
      <div className="ambient ambient-three" />

      <div className="app-content">
        <SearchForm onSearch={handleSearch} loading={loading} />

        {error && (
          <div className="error-banner" role="alert">
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        {loading && !overview && (
          <div className="loading-state">
            <div className="loading-orbit"><Loader2 size={22} /></div>
            <p>Reading the sky...</p>
            <span>Connecting to OpenWeather</span>
          </div>
        )}

        {overview && <WeatherCard overview={overview} />}

        {!overview && !loading && !error && (
          <div className="welcome-state">
            <div className="welcome-icon"><Compass size={30} /></div>
            <p className="eyebrow">Your personal weather window</p>
            <h1>Find the feeling<br /><em>outside.</em></h1>
            <p className="welcome-copy">Search for a city to see a live, detailed forecast with the little things that make planning your day easier.</p>
            <div className="suggestions"><span>Try</span><button type="button" onClick={() => handleSearch('New Delhi', 'metric')}>New Delhi</button><button type="button" onClick={() => handleSearch('London', 'metric')}>London</button><button type="button" onClick={() => handleSearch('Tokyo', 'metric')}>Tokyo</button></div>
          </div>
        )}

        <footer className="app-footer"><span>Weather App</span><span>Powered by OpenWeather API</span></footer>
      </div>
    </div>
  );
}
