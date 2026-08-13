# WeatherApp

WeatherApp is a two-part weather application: a Spring Boot service that talks to OpenWeatherMap and a responsive Next.js dashboard with a glassmorphism UI.

## What it includes

- Aether dashboard at `http://localhost:3000` with city search and Celsius/Fahrenheit support.
- Current conditions: temperature, feels-like temperature, daily high/low, humidity, pressure, wind, visibility, and cloud cover.
- Sunrise and sunset times plus the next eight three-hour forecast points with rain chances.
- Spring Boot REST API at `http://localhost:9090`.
- The original Vaadin view remains available from the Spring Boot application for compatibility.
- OpenWeatherMap credentials stay on the backend and are not exposed to the browser.

## Requirements

- Java 17+
- Node.js 18+ and npm
- Internet access for OpenWeatherMap requests
- An OpenWeatherMap API key

## Local development

### 1. Configure the API key

The backend reads `OPENWEATHERMAP_API_KEY`. The older `key` variable is also supported for compatibility.

macOS/Linux:

```bash
export OPENWEATHERMAP_API_KEY=YOUR_API_KEY
```

Windows PowerShell:

```powershell
$env:OPENWEATHERMAP_API_KEY="YOUR_API_KEY"
```

Do not commit real API keys. The repository includes [`env.example`](/Users/Apple/IdeaProjects/WeatherApp/env.example) as a template.

### 2. Start the Spring Boot backend

From the repository root:

```bash
bash mvnw spring-boot:run
```

The backend listens on port `9090` by default.

### 3. Start the Next.js dashboard

In a second terminal:

```bash
cd weather-app-nextjs
npm ci
npm run dev
```

Open [`http://localhost:3000`](http://localhost:3000). The frontend uses the same-origin `/api/weather` path; Next.js proxies it to the backend. To point it at another backend, set:

```bash
export NEXT_PUBLIC_API_URL=https://your-backend.example.com/api/weather
```

## REST API

All endpoints accept `city` and an optional `unit` (`metric` or `imperial`).

| Endpoint | Purpose |
| --- | --- |
| `GET /api/weather/current?city=London&unit=metric` | Raw current-weather shape used by integrations |
| `GET /api/weather/details?city=London&unit=metric` | Normalized current weather details |
| `GET /api/weather/overview?city=London&unit=metric` | Normalized current details plus the next eight three-hour forecast points; used by the Next.js dashboard |

Example request:

```bash
curl "http://localhost:9090/api/weather/overview?city=London&unit=metric"
```

## Docker Compose

See [`README-Docker.md`](/Users/Apple/IdeaProjects/WeatherApp/README-Docker.md) for the container workflow. The intended entry point is:

```bash
cp env.example .env
# Set OPENWEATHERMAP_API_KEY in .env
docker compose up --build
```

The dashboard is exposed on port `3000`. The backend is bound to `127.0.0.1:9090` and is reached through the Next.js proxy, so it is not directly exposed to the public internet.

## Railway deployment

Railway is a simpler alternative to managing a VPS. Deploy the repository as two services in one Railway project:

1. **Backend service:** use the repository root, set `RAILWAY_DOCKERFILE_PATH=Dockerfile.backend`, and deploy the custom Dockerfile.
2. **Frontend service:** use `weather-app-nextjs` as the root directory and its `Dockerfile`.

Set these Railway variables:

| Service | Variable | Value |
| --- | --- | --- |
| Backend | `OPENWEATHERMAP_API_KEY` | Your OpenWeatherMap key |
| Backend | `PORT` | `9090` |
| Frontend | `BACKEND_URL` | `http://${{Backend.RAILWAY_PRIVATE_DOMAIN}}:${{Backend.PORT}}` |

Give only the frontend service a public Railway domain. The frontend calls its same-origin `/api/weather` route and the Next.js rewrite sends the request privately to the backend.

Railway’s Free plan includes $1 of monthly resource credit. Hobby is $5/month and includes $5 of resource usage; CPU, memory, and network usage above that can add charges. It is excellent for trying the app, but a small VPS may be cheaper for an always-on two-service deployment. See the [Railway pricing documentation](https://docs.railway.com/pricing/plans).

## Low-cost VPS deployment

For this two-container app, use one Linux VPS with at least 2 vCPUs and 4 GB RAM. That gives the JVM and Next.js server enough headroom while keeping the deployment inexpensive. Hetzner Cloud is usually the lowest-cost practical option; DigitalOcean is a simpler alternative with more predictable beginner documentation. Provider availability and prices vary by region, so choose the nearest location to your users.

On a fresh Ubuntu/Debian VPS:

```bash
sudo apt update
sudo apt install -y git docker.io docker-compose-plugin
sudo systemctl enable --now docker
git clone <your-repository-url> WeatherApp
cd WeatherApp
cp env.example .env
nano .env
docker compose up --build -d
docker compose ps
```

Open `http://YOUR_SERVER_IP:3000`. For a real public deployment, put a domain and HTTPS reverse proxy (Caddy, Nginx, or Cloudflare Tunnel) in front of port `3000`; keep port `9090` private. Update the application with:

```bash
git pull
docker compose up --build -d
```

## Verification

Backend tests:

```bash
bash mvnw test
```

Frontend type-check and production build:

```bash
cd weather-app-nextjs
npx tsc --noEmit
npm run build
```

## Project structure

```text
WeatherApp/
├── src/main/java/...              # Spring Boot API and legacy Vaadin view
├── src/main/resources/            # Backend configuration and static assets
├── weather-app-nextjs/app/        # Next.js page, layout, and global styles
├── weather-app-nextjs/components/ # Search and weather dashboard components
├── weather-app-nextjs/lib/api.ts  # Typed frontend API client
├── Dockerfile.backend
├── docker-compose.yml
└── env.example
```

## Main technologies

- Java 17, Spring Boot 2.7, OkHttp, and `org.json`
- Next.js 14, React 18, TypeScript, Tailwind CSS, Framer Motion, and Lucide React
- OpenWeatherMap Current Weather and 5-day/3-hour Forecast APIs

## License

This project is for demonstration purposes.
