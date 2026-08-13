# Use Node.js 18 as base image
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# The internal Docker hostname is used by the server-side rewrite. It is not
# sent to the browser.
ARG BACKEND_URL=http://localhost:9090
ENV BACKEND_URL=${BACKEND_URL}

# Build the application
RUN npm run build

# Production stage
FROM node:18-alpine AS runner

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install only production dependencies
RUN npm ci --only=production

# Copy built application
COPY --from=builder /app/.next ./.next

# Expose port 3000
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
