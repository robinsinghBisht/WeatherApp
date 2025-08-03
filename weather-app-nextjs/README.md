# Weather App - Next.js Frontend

A modern React Next.js application that consumes weather data from your Java Spring Boot backend.

## Features

- 🌤️ Real-time weather data for any city
- 🌡️ Temperature in Celsius or Fahrenheit
- 💨 Wind speed and direction
- 💧 Humidity and pressure information
- 📱 Responsive design for mobile and desktop
- ⚡ Fast and modern UI with Tailwind CSS

## Prerequisites

- Node.js 18+ and npm
- Your Java Spring Boot backend running on port 9090
- OpenWeatherMap API key configured in your backend

## Setup

1. **Install dependencies:**
   ```bash
   cd weather-app-nextjs
   npm install
   ```

2. **Start the development server:**
   ```bash
   npm run dev
   ```

3. **Open your browser:**
   Navigate to [http://localhost:3000](http://localhost:3000)

## API Endpoints

The app communicates with your Java backend at `http://localhost:9090/api/weather`:

- `GET /api/weather/current?city={city}&unit={unit}` - Get current weather
- `GET /api/weather/details?city={city}&unit={unit}` - Get detailed weather information

## Project Structure

```
weather-app-nextjs/
├── app/                    # Next.js 13+ app directory
│   ├── globals.css        # Global styles
│   ├── layout.tsx         # Root layout
│   └── page.tsx           # Main page component
├── components/            # React components
│   ├── SearchForm.tsx     # City search form
│   └── WeatherCard.tsx    # Weather display card
├── lib/                   # Utility functions
│   └── api.ts            # API service functions
└── package.json          # Dependencies and scripts
```

## Technologies Used

- **Next.js 14** - React framework
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **Lucide React** - Icons
- **Axios** - HTTP client

## Development

- **Hot reload:** Changes are reflected immediately
- **TypeScript:** Full type safety and IntelliSense
- **ESLint:** Code quality and consistency
- **Responsive:** Mobile-first design

## Building for Production

```bash
npm run build
npm start
```

## Environment Variables

Make sure your Java backend is running on `http://localhost:9090` or update the API base URL in `lib/api.ts`.

## Troubleshooting

1. **CORS Issues:** Ensure your Java backend has CORS enabled (already configured in WeatherController)
2. **API Connection:** Verify your backend is running on port 9090
3. **Weather Data:** Check that your OpenWeatherMap API key is properly configured

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is for demonstration purposes. 