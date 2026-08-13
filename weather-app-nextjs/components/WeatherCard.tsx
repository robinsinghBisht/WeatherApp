'use client';

import { motion } from 'framer-motion';
import {
  Activity,
  Cloud,
  CloudRain,
  Droplets,
  Eye,
  Gauge,
  Sunrise,
  Sunset,
  Thermometer,
  Wind,
} from 'lucide-react';
import { ForecastItem, WeatherOverview } from '@/lib/api';

interface WeatherCardProps {
  overview: WeatherOverview;
}

const iconUrl = (icon: string) => `https://openweathermap.org/img/wn/${icon}@2x.png`;

function clock(timestamp: number, timezone: number) {
  if (!timestamp) return '--';
  return new Intl.DateTimeFormat('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
    timeZone: 'UTC',
  }).format(new Date((timestamp + timezone) * 1000));
}

function forecastHour(timestamp: number, timezone: number) {
  return new Intl.DateTimeFormat('en-US', {
    hour: 'numeric',
    timeZone: 'UTC',
  }).format(new Date((timestamp + timezone) * 1000));
}

function greeting(timestamp: number, timezone: number) {
  const localHour = Number(new Intl.DateTimeFormat('en-US', {
    hour: 'numeric', hour12: false, timeZone: 'UTC',
  }).format(new Date((timestamp + timezone) * 1000)));
  if (localHour < 12) return 'Good morning';
  if (localHour < 18) return 'Good afternoon';
  return 'Good evening';
}

function unitLabel(unit: WeatherOverview['current']['unit']) {
  return unit === 'imperial' ? '°F' : '°C';
}

function speedLabel(unit: WeatherOverview['current']['unit']) {
  return unit === 'imperial' ? 'mph' : 'm/s';
}

function visibilityLabel(meters: number, unit: WeatherOverview['current']['unit']) {
  const distance = unit === 'imperial' ? meters / 1609.34 : meters / 1000;
  return `${distance.toFixed(1)} ${unit === 'imperial' ? 'mi' : 'km'}`;
}

function WeatherMetric({ icon, label, value, tone }: {
  icon: React.ReactNode;
  label: string;
  value: string;
  tone: string;
}) {
  return (
    <div className="metric-tile">
      <div className={`metric-icon ${tone}`}>{icon}</div>
      <div>
        <p className="metric-label">{label}</p>
        <p className="metric-value">{value}</p>
      </div>
    </div>
  );
}

function ForecastTile({ item, timezone, unit }: { item: ForecastItem; timezone: number; unit: WeatherOverview['current']['unit'] }) {
  return (
    <div className="forecast-tile">
      <p className="forecast-time">{forecastHour(item.timestamp, timezone)}</p>
      <img src={iconUrl(item.icon)} alt={item.description} className="forecast-icon" />
      <p className="forecast-temp">{Math.round(item.temperature)}{unitLabel(unit)}</p>
      <div className="forecast-rain"><CloudRain size={12} /> {Math.round(item.rainChance)}%</div>
    </div>
  );
}

export default function WeatherCard({ overview }: WeatherCardProps) {
  const { current, forecast } = overview;
  const unit = unitLabel(current.unit);
  const rainNow = current.description.includes('rain') || current.description.includes('drizzle');
  const tip = rainNow
    ? 'Keep an umbrella close — showers may stick around.'
    : current.humidity > 70
      ? 'The air is feeling humid today. Stay hydrated.'
      : 'A comfortable window to get outside and enjoy the day.';

  return (
    <motion.main
      initial={{ opacity: 0, y: 18 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.45 }}
      className="weather-dashboard"
    >
      <section className="hero-panel glass-card">
        <div className="hero-heading">
          <div>
            <p className="eyebrow">{greeting(current.observedAt || Date.now() / 1000, current.timezone)}</p>
            <h1>{current.city}<span>{current.country ? `, ${current.country}` : ''}</span></h1>
            <p className="coordinates">{current.lat.toFixed(2)}° N&nbsp;&nbsp; {Math.abs(current.lon).toFixed(2)}° {current.lon >= 0 ? 'E' : 'W'}</p>
          </div>
          <div className="live-pill"><span className="live-dot" /> Live now</div>
        </div>

        <div className="hero-weather">
          <div className="weather-orb">
            <div className="orb-glow" />
            <img src={iconUrl(current.icon)} alt={current.description} />
          </div>
          <div className="temperature-block">
            <p className="temperature">{Math.round(current.temperature)}<sup>{unit}</sup></p>
            <p className="condition">{current.description}</p>
            <p className="feels">Feels like {Math.round(current.feelsLike)}{unit}</p>
          </div>
        </div>

        <div className="range-row">
          <span><span className="range-dot high" /> High <strong>{Math.round(current.tempMax)}{unit}</strong></span>
          <span><span className="range-dot low" /> Low <strong>{Math.round(current.tempMin)}{unit}</strong></span>
          <span className="range-divider" />
          <span className="weather-tip"><Activity size={14} /> {tip}</span>
        </div>
      </section>

      <section className="insight-panel glass-card">
        <div className="section-heading">
          <div><p className="eyebrow">Atmosphere</p><h2>At a glance</h2></div>
          <Cloud size={24} className="section-heading-icon" />
        </div>
        <div className="metrics-grid">
          <WeatherMetric icon={<Droplets size={18} />} label="Humidity" value={`${current.humidity}%`} tone="blue" />
          <WeatherMetric icon={<Wind size={18} />} label="Wind" value={`${current.windSpeed.toFixed(1)} ${speedLabel(current.unit)}`} tone="lavender" />
          <WeatherMetric icon={<Gauge size={18} />} label="Pressure" value={`${current.pressure} hPa`} tone="amber" />
          <WeatherMetric icon={<Eye size={18} />} label="Visibility" value={visibilityLabel(current.visibility, current.unit)} tone="mint" />
        </div>
        <div className="sun-row">
          <div><Sunrise size={18} /><span>Sunrise</span><strong>{clock(current.sunrise, current.timezone)}</strong></div>
          <div><Sunset size={18} /><span>Sunset</span><strong>{clock(current.sunset, current.timezone)}</strong></div>
        </div>
      </section>

      <section className="forecast-panel glass-card">
        <div className="section-heading">
          <div><p className="eyebrow">The next few hours</p><h2>Hourly forecast</h2></div>
          <span className="forecast-caption">Rain chance</span>
        </div>
        <div className="forecast-list">
          {forecast.length > 0 ? forecast.slice(0, 6).map((item) => (
            <ForecastTile key={item.timestamp} item={item} timezone={current.timezone} unit={current.unit} />
          )) : <p className="empty-state">Forecast data is not available right now.</p>}
        </div>
      </section>

      <section className="details-panel glass-card">
        <div className="section-heading">
          <div><p className="eyebrow">More detail</p><h2>Today&apos;s conditions</h2></div>
          <Thermometer size={24} className="section-heading-icon" />
        </div>
        <div className="detail-list">
          <div><span>Cloud cover</span><strong>{current.clouds}%</strong><div className="progress"><i style={{ width: `${Math.min(current.clouds, 100)}%` }} /></div></div>
          <div><span>Wind direction</span><strong>{current.windDirection} <small>{current.windDegree}°</small></strong><div className="progress"><i style={{ width: `${Math.min((current.windDegree / 360) * 100, 100)}%` }} /></div></div>
          <div><span>Temperature spread</span><strong>{Math.round(current.tempMax - current.tempMin)}{unit}</strong><div className="progress"><i style={{ width: `${Math.min(Math.max((current.tempMax - current.tempMin) * 8, 10), 100)}%` }} /></div></div>
        </div>
      </section>
    </motion.main>
  );
}
