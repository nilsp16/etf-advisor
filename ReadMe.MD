# ETF Advisor

A full-stack web application for monitoring Exchange-Traded Funds with live market data, automated trading recommendations, and a personal watchlist. Built as a university project at DHBW Ravensburg.

## Features

- Browse and manage ETFs with detailed metadata (ISIN, TER, dividend policy, replication method)
- Live market data from the Alpaca Trading API (current price, daily OHLCV)
- Automated recommendation engine with a scoring algorithm based on 52-week range and daily price movement
- Scheduled recommendation generation every 60 seconds for all tracked ETFs
- Personal watchlist per user — add ETFs by ticker with automatic metadata lookup via API Ninjas
- JWT authentication with role-based access control (ADMIN / USER)
- Admin panel for managing ETF metadata
- Dark / light mode in the frontend
- Swagger/OpenAPI documentation

## Architecture

```
┌──────────────┐         ┌──────────────────────────────────────┐
│              │  REST   │           Spring Boot 4               │
│  React       │◄───────►│                                      │
│  Frontend    │  JSON   │  Controller → Service → Repository   │
│  (Vite/TS)   │         │                  │                   │
│              │         │           ┌──────┴──────┐            │
│  Port 5173   │         │           │  Alpaca API │            │
└──────────────┘         │           │  Ninjas API │            │
                         │           └─────────────┘            │
                         │         Port 8080                    │
                         └──────────────┬───────────────────────┘
                                        │
                                        ▼
                                 ┌──────────────┐
                                 │  PostgreSQL   │
                                 │  Port 5432    │
                                 └──────────────┘
```

### Backend layers

| Layer | Responsibility | Spring annotation |
|-------|---------------|-------------------|
| Controller | HTTP endpoints, request/response mapping | `@RestController` |
| Service | Business logic, scoring, scheduling | `@Service` |
| Repository | Database access via JPA | `JpaRepository` |
| Entity | Data model | `@Entity` |
| Client | External API communication | `@Service` + `RestClient` |
| Security | JWT auth, role-based access | `@Component`, `@Configuration` |

### Entity relationships

```
Etf (1) ──── (n) Recommendation
Etf (1) ──── (n) WatchlistEntry
User (1) ──── (n) WatchlistEntry
```

- **Etf** — ETF master data (name, ISIN, ticker, TER, 52-week range, etc.)
- **Recommendation** — generated trading signal (BUY/HOLD/SELL) with score and reasoning
- **WatchlistEntry** — user-specific bookmark of an ETF with optional note
- **User** — application user with username, hashed password, and role (ADMIN/USER)

## External APIs

### Alpaca Trading API
- **Purpose:** Live market data (current price, daily OHLCV bars)
- **Endpoint used:** `GET /v2/stocks/{symbol}/snapshot`
- **Auth:** API key + secret via headers `APCA-API-KEY-ID` and `APCA-API-SECRET-KEY`
- **Website:** https://alpaca.markets
- **Free tier:** Paper trading account, unlimited requests

### API Ninjas ETF API
- **Purpose:** ETF metadata (name, ISIN, country) when adding by ticker
- **Endpoint used:** `GET /v1/etf?ticker={ticker}`
- **Auth:** API key via header `X-Api-Key`
- **Website:** https://api-ninjas.com
- **Free tier:** 50,000 requests/month

## Recommendation scoring algorithm

The scoring engine combines two indicators to generate BUY, HOLD, or SELL signals:

1. **52-week range position** — if the current price is in the lower third of the 52-week range → +1 buy score; upper third → +1 sell score
2. **Daily open comparison** — if the current price is more than 1% below the daily open → +1 buy score; more than 1% above → +1 sell score

The final signal is determined by the difference: `buyScore - sellScore > 0` → BUY, `< 0` → SELL, `= 0` → HOLD.

## Tech stack

| Component | Technology |
|-----------|-----------|
| Backend | Java 21, Spring Boot 4, Spring Security, JWT |
| Frontend | React, TypeScript, Vite |
| Database | PostgreSQL 17 (via Docker) |
| Build tool | Maven |
| API docs | Swagger / OpenAPI (springdoc) |
| Containerization | Docker, Docker Compose |
| External APIs | Alpaca Trading API, API Ninjas ETF API |

## Prerequisites

- Java 21
- Node.js 20+
- Docker Desktop
- Alpaca API key (free paper trading account at https://alpaca.markets)
- API Ninjas key (free at https://api-ninjas.com)

## Getting started

### Option A: Docker Compose (recommended)

The fastest way to start — one command runs everything.

**1. Clone and configure:**

```bash
git clone https://github.com/nilsp16/etf-advisor.git
cd etf-advisor
```

**2. Create a `.env` file** in the project root:

```
ALPACA_API_KEY=your_alpaca_key
ALPACA_API_SECRET=your_alpaca_secret
NINJAS_API_KEY=your_ninjas_key
JWT_SECRET=your_jwt_secret_min_32_chars
```

**3. Start everything:**

```bash
docker compose up --build
```

This builds the backend image, starts PostgreSQL, and launches the application. The backend is available at http://localhost:8080.

**4. Start the frontend** (in a separate terminal):

```bash
cd etf-advisor-frontend
npm install
npm run dev
```

**5. Open** http://localhost:5173 and log in with `admin` / `admin123`.

**6. Stop everything:**

```bash
docker compose down
```

### Option B: Manual setup

**1. Start PostgreSQL via Docker:**

```bash
docker run -d \
  --name etf-postgres \
  -e POSTGRES_DB=etfadvisor \
  -e POSTGRES_USER=sa \
  -e POSTGRES_PASSWORD=secret \
  -p 5432:5432 \
  postgres:17
```

**2. Configure API keys** as environment variables in your IDE or terminal:

```
ALPACA_API_KEY=your_alpaca_key
ALPACA_API_SECRET=your_alpaca_secret
NINJAS_API_KEY=your_ninjas_key
JWT_SECRET=your_jwt_secret_min_32_chars
```

**3. Start the backend:**

```bash
./mvnw spring-boot:run
```

**4. Start the frontend:**

```bash
cd etf-advisor-frontend
npm install
npm run dev
```

**5. Open** http://localhost:5173 and log in with `admin` / `admin123`.

## API documentation

Swagger UI is available at http://localhost:8080/swagger-ui.html when the backend is running. It provides interactive documentation for all endpoints.

### Public (no auth required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |
| GET | `/api/etfs` | List all ETFs |
| GET | `/api/etfs/{id}` | Get ETF by ID |
| GET | `/api/etfs/ticker/{ticker}` | Get ETF by ticker |
| GET | `/api/etfs/isin/{isin}` | Get ETF by ISIN |
| GET | `/api/market-data/{etfId}` | Get live market data for an ETF |

### Authenticated (any role)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/watchlist` | Get current user's watchlist |
| POST | `/api/watchlist` | Add ETF to watchlist |
| POST | `/api/watchlist/add-by-ticker` | Add ETF by ticker (auto-creates if needed) |
| DELETE | `/api/watchlist/{id}` | Remove from watchlist |
| GET | `/api/recommendations` | Get all recommendations |
| GET | `/api/recommendations/etf/{etfId}` | Get recommendations for an ETF |
| POST | `/api/recommendations/generate/{etfId}` | Generate a new recommendation |

### Admin only

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/etfs` | Create a new ETF |
| PUT | `/api/etfs/{id}` | Update ETF metadata |
| DELETE | `/api/etfs/{id}` | Delete an ETF |

## Testing

Run all tests:

```bash
./mvnw test
```

The test suite includes 12 tests across 6 test classes:

- **Unit tests:** EtfMapper, RecommendationMapper
- **Integration tests:** JwtUtil (token generation/validation)
- **API tests:** EtfController, AuthController (MockMvc)
- **Repository tests:** EtfRepository (derived queries)

Tests use H2 in-memory database via `application-test.properties` and run without Docker.

## Project structure

```
etf-advisor/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
├── src/main/java/de/dhbwravensburg/etfadvisor/
│   ├── EtfAdvisorApplication.java
│   ├── client/                    # External API clients
│   │   ├── AlpacaMarketDataClient.java
│   │   └── NinjasEtfClient.java
│   ├── config/                    # Spring configuration
│   │   ├── AlpacaApiConfig.java
│   │   ├── NinjasApiConfig.java
│   │   ├── CorsConfig.java
│   │   └── DataSeeder.java
│   ├── controller/                # REST controllers
│   │   ├── AuthController.java
│   │   ├── EtfController.java
│   │   ├── MarketDataController.java
│   │   ├── RecommendationController.java
│   │   └── WatchlistEntryController.java
│   ├── dto/                       # Data Transfer Objects
│   │   ├── alpaca/
│   │   └── ...Request/Response records
│   ├── entity/                    # JPA entities
│   │   ├── Etf.java
│   │   ├── Recommendation.java
│   │   ├── WatchlistEntry.java
│   │   ├── User.java
│   │   ├── Role.java
│   │   └── Signal.java
│   ├── exceptions/                # Custom exceptions + global handler
│   ├── mapper/                    # DTO ↔ Entity mappers
│   ├── repository/                # Spring Data JPA repositories
│   ├── scheduler/                 # Scheduled tasks
│   │   └── RecommendationScheduler.java
│   ├── security/                  # JWT + Spring Security
│   │   ├── JwtUtil.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── CustomUserDetailService.java
│   │   └── SecurityConfig.java
│   └── service/                   # Business logic
├── src/test/                      # Test suite
├── etf-advisor-frontend/          # React frontend
│   ├── src/
│   │   ├── api/                   # Typed API layer
│   │   ├── components/            # React components
│   │   ├── theme/                 # Dark/light mode + auth context
│   │   └── types.ts               # TypeScript domain types
│   └── vite.config.ts             # Dev proxy configuration
└── .env                           # API keys (not in Git)
```

## License

University project — DHBW Ravensburg, Web Engineering 2.