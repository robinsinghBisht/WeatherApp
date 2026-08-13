import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'Aether — Weather, in its clearest form',
  description: 'A calm, glassy weather dashboard powered by OpenWeather.',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  )
}
