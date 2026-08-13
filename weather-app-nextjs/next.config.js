/** @type {import('next').NextConfig} */
const backendUrl = process.env.BACKEND_URL || 'http://localhost:9090';

const nextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/weather/:path*',
        destination: `${backendUrl}/api/weather/:path*`,
      },
    ];
  },
};

module.exports = nextConfig;
