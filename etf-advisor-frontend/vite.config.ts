import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// The dev proxy forwards every /api request to the Spring Boot backend,
// so the browser sees a single origin and there is no CORS issue.
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
