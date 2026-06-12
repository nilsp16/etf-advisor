// Central API layer with JWT auth support.
// Public endpoints (GET etfs, market-data) work without token.
// Protected endpoints send the Authorization: Bearer header.

import type {
  Etf,
  EtfInput,
  Recommendation,
  WatchlistEntry,
  WatchlistInput,
  MarketData,
  AuthResponse,
} from '../types'

const BASE = '/api'

let _token: string | null = null
export function setApiToken(token: string | null) {
  _token = token
}

function authHeaders(): Record<string, string> {
  const h: Record<string, string> = { 'Content-Type': 'application/json' }
  if (_token) h['Authorization'] = `Bearer ${_token}`
  return h
}

async function handle<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let message = `HTTP ${response.status}`
    try {
      const problem = await response.json()
      if (problem?.detail) message = problem.detail
    } catch { /* no JSON body */ }
    throw new Error(message)
  }
  if (response.status === 204) return undefined as T
  return (await response.json()) as T
}

// --- Auth (public) ---

export function register(username: string, password: string, role: string): Promise<AuthResponse> {
  return fetch(`${BASE}/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password, role }),
  }).then((r) => handle<AuthResponse>(r))
}

export function login(username: string, password: string): Promise<AuthResponse> {
  return fetch(`${BASE}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  }).then((r) => handle<AuthResponse>(r))
}

// --- ETFs ---

export function getEtfs(): Promise<Etf[]> {
  return fetch(`${BASE}/etfs`).then((r) => handle<Etf[]>(r))
}

export function getEtf(id: number): Promise<Etf> {
  return fetch(`${BASE}/etfs/${id}`).then((r) => handle<Etf>(r))
}

export function createEtf(input: EtfInput): Promise<Etf> {
  return fetch(`${BASE}/etfs`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(input),
  }).then((r) => handle<Etf>(r))
}

export function updateEtf(id: number, input: EtfInput): Promise<Etf> {
  return fetch(`${BASE}/etfs/${id}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(input),
  }).then((r) => handle<Etf>(r))
}

export function deleteEtf(id: number): Promise<void> {
  return fetch(`${BASE}/etfs/${id}`, {
    method: 'DELETE',
    headers: authHeaders(),
  }).then((r) => handle<void>(r))
}

// --- Market data ---

export function getMarketData(etfId: number): Promise<MarketData> {
  return fetch(`${BASE}/market-data/${etfId}`).then((r) => handle<MarketData>(r))
}

// --- Recommendations ---

export function getRecommendations(): Promise<Recommendation[]> {
  return fetch(`${BASE}/recommendations`, { headers: authHeaders() }).then((r) =>
      handle<Recommendation[]>(r),
  )
}

export function getRecommendationsForEtf(etfId: number): Promise<Recommendation[]> {
  return fetch(`${BASE}/recommendations/etf/${etfId}`, { headers: authHeaders() }).then((r) =>
      handle<Recommendation[]>(r),
  )
}

export function generateRecommendation(etfId: number): Promise<Recommendation> {
  return fetch(`${BASE}/recommendations/generate/${etfId}`, {
    method: 'POST',
    headers: authHeaders(),
  }).then((r) => handle<Recommendation>(r))
}

// --- Watchlist ---

export function getWatchlist(): Promise<WatchlistEntry[]> {
  return fetch(`${BASE}/watchlist`, { headers: authHeaders() }).then((r) =>
      handle<WatchlistEntry[]>(r),
  )
}

export function addToWatchlist(input: WatchlistInput): Promise<WatchlistEntry> {
  return fetch(`${BASE}/watchlist`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(input),
  }).then((r) => handle<WatchlistEntry>(r))
}

export function addToWatchlistByTicker(ticker: string, userNote: string): Promise<WatchlistEntry> {
  return fetch(`${BASE}/watchlist/add-by-ticker`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ ticker, userNote }),
  }).then((r) => handle<WatchlistEntry>(r))
}

export function removeFromWatchlist(id: number): Promise<void> {
  return fetch(`${BASE}/watchlist/${id}`, {
    method: 'DELETE',
    headers: authHeaders(),
  }).then((r) => handle<void>(r))
}