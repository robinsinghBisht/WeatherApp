# WeatherApp - Docker Deployment

This guide explains how to deploy the Weather App using Docker Compose, running both the Java backend and Next.js frontend in containers.

## 🐳 Prerequisites

- Docker and Docker Compose installed
- OpenWeatherMap API key

## 📁 Project Structure

```
WeatherApp/
├── Dockerfile.backend          # Java backend Dockerfile
├── docker-compose.yml          # Docker Compose configuration
├── .dockerignore              # Docker ignore file
├── env.example                # Environment variables example
├── weather-app-nextjs/        # Next.js frontend
│   ├── Dockerfile            # Frontend Dockerfile
│   └── .dockerignore         # Frontend Docker ignore
└── README-Docker.md          # This file
```

## 🚀 Quick Start

### 1. Set up Environment Variables

Copy the example environment file and add your API key:

```bash
cp env.example .env
```

Edit `.env` and add your OpenWeatherMap API key:
```bash
OPENWEATHERMAP_API_KEY=your_actual_api_key_here
```

### 2. Build and Start Services

```bash
# Build and start both services
docker-compose up --build

# Or run in detached mode
docker-compose up --build -d
```

### 3. Access the Application

- **Frontend:** http://localhost:3000
- **Backend API:** available privately at `127.0.0.1:9090` and proxied through the frontend at `/api/weather`

## 🔧 Docker Services

### Backend Service
- **Image:** Java 17 with Spring Boot
- **Port:** 9090
- **Health Check:** API endpoint test
- **Environment:** OpenWeatherMap API key

### Frontend Service
- **Image:** Node.js 18 with Next.js
- **Port:** 3000
- **Dependencies:** Waits for backend to be healthy
- **Environment:** Uses an internal Next.js rewrite to reach `http://backend:9090`

## 🛠️ Docker Commands

```bash
# Start all services
docker-compose up

# Start in detached mode
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# View logs for specific service
docker-compose logs -f backend
docker-compose logs -f frontend

# Rebuild and restart
docker-compose up --build

# Remove all containers and volumes
docker-compose down -v
```

## 🔍 Troubleshooting

### Common Issues

1. **API Key Not Set**
   ```bash
   # Check if environment variable is set
   docker-compose logs backend
   ```

2. **Frontend Can't Connect to Backend**
   ```bash
   # Check if backend is healthy
   curl "http://localhost:9090/api/weather/current?city=London&unit=metric"
   ```

3. **Port Already in Use**
   ```bash
   # Check what's using the ports
   lsof -i :3000
   lsof -i :9090
   ```

### Health Checks

The services include health checks to ensure they're running properly:

- **Backend:** Tests API endpoint
- **Frontend:** Tests web server

### Network Configuration

Services communicate through a custom Docker network:
- **Network:** `weather-network`
- **Backend URL:** `http://backend:9090` (internal)
- **Frontend URL:** `http://localhost:3000` (external)

## 🔄 Development Workflow

### Local Development
```bash
# Start only backend in Docker
docker-compose up backend

# Run frontend locally
cd weather-app-nextjs
npm run dev
```

### Production Deployment
```bash
# Build for production
docker-compose -f docker-compose.yml up --build -d

# Check service status
docker-compose ps
```

## 📊 Monitoring

### View Service Status
```bash
docker-compose ps
```

### View Resource Usage
```bash
docker stats
```

### View Logs
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs backend
docker-compose logs frontend
```

## 🔒 Security Notes

- API key is passed as environment variable
- Services run in isolated containers
- Network communication is internal
- External access only through exposed ports

## 🚀 Production Considerations

1. **Environment Variables:** Use proper secrets management
2. **SSL/TLS:** Add reverse proxy with HTTPS
3. **Monitoring:** Add logging and monitoring solutions
4. **Scaling:** Consider container orchestration (Kubernetes)

## 📝 Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `OPENWEATHERMAP_API_KEY` | Your OpenWeatherMap API key | Yes |
| `NEXT_PUBLIC_API_URL` | Optional separate backend URL; same-origin proxy is used by default | No |

## 🎯 Next Steps

1. Set up your OpenWeatherMap API key
2. Run `docker-compose up --build`
3. Access the app at http://localhost:3000
4. Test weather search functionality

## 📞 Support

If you encounter issues:
1. Check the logs: `docker-compose logs`
2. Verify environment variables
3. Ensure ports are available
4. Check Docker and Docker Compose versions
