# ETF Advisor — Frontend

React + TypeScript frontend for the ETF Advisor backend. Monochrome design with
dark/light mode, built following the lecture pattern (Vite, typed API layer,
component tree, controlled forms).

## Setup

1. Create the Vite project next to your backend folder:

   ```bash
   npm create vite@latest etf-advisor-frontend -- --template react-ts
   cd etf-advisor-frontend
   npm install
   ```

2. Copy the contents of this `frontend/` folder into the project, replacing the
   generated `src/`, `vite.config.ts`, etc. The structure is:

   ```
   src/
   ├── main.tsx
   ├── App.tsx
   ├── App.css
   ├── index.css
   ├── types.ts
   ├── api/etfAdvisorApi.ts
   ├── theme/ThemeContext.tsx
   └── components/
       ├── EtfTable.tsx
       ├── EtfDetail.tsx
       ├── EtfForm.tsx
       ├── WatchlistPanel.tsx
       └── SignalBadge.tsx
   vite.config.ts
   ```

3. Start the backend (`./mvnw spring-boot:run`) and the PostgreSQL container,
   then start the frontend:

   ```bash
   npm run dev
   ```

4. Open http://localhost:5173

## How it talks to the backend

The frontend never hardcodes `localhost:8080`. It uses relative `/api` URLs.
The Vite dev proxy (in `vite.config.ts`) forwards those to the Spring Boot
backend, so the browser stays on one origin and there is no CORS error.

## Features

- **List view** — all ETFs in a table (click a row to select)
- **Detail view** — selected ETF with live Alpaca market data
- **Form** — create a new ETF (POST /api/etfs)
- **Recommendation generation** — calls POST /api/recommendations/generate/{id}
- **Watchlist** — add and remove entries
- **Dark/Light mode** — toggle in the header, follows OS preference on first load
