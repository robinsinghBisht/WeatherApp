'use client';

import { FormEvent, useState } from 'react';
import { Loader2, MapPin, Search, SlidersHorizontal } from 'lucide-react';
import { motion } from 'framer-motion';
import { TemperatureUnit } from '@/lib/api';

interface SearchFormProps {
  onSearch: (city: string, unit: TemperatureUnit) => void;
  loading: boolean;
}

export default function SearchForm({ onSearch, loading }: SearchFormProps) {
  const [city, setCity] = useState('');
  const [unit, setUnit] = useState<TemperatureUnit>('metric');

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const nextCity = city.trim();
    if (nextCity) onSearch(nextCity, unit);
  };

  return (
    <motion.form
      initial={{ opacity: 0, y: -8 }}
      animate={{ opacity: 1, y: 0 }}
      onSubmit={handleSubmit}
      className="search-shell"
    >
      <div className="search-brand">
        <div className="brand-mark"><span /></div>
        <div>
          <p className="brand-name">Aether</p>
          <p className="brand-caption">Weather, in its clearest form</p>
        </div>
      </div>

      <div className="search-controls">
        <label className="search-input-wrap" htmlFor="city">
          <MapPin size={18} aria-hidden="true" />
          <input
            id="city"
            value={city}
            onChange={(event) => setCity(event.target.value)}
            placeholder="Search a city..."
            autoComplete="off"
            aria-label="City name"
          />
        </label>

        <label className="unit-select-wrap" htmlFor="unit">
          <SlidersHorizontal size={16} aria-hidden="true" />
          <select id="unit" value={unit} onChange={(event) => setUnit(event.target.value as TemperatureUnit)}>
            <option value="metric">°C</option>
            <option value="imperial">°F</option>
          </select>
        </label>

        <motion.button
          whileTap={{ scale: 0.96 }}
          type="submit"
          disabled={loading || !city.trim()}
          className="search-button"
          aria-label="Search weather"
        >
          {loading ? <Loader2 size={18} className="spin" /> : <Search size={18} />}
          <span className="search-button-label">Search</span>
        </motion.button>
      </div>
    </motion.form>
  );
}
