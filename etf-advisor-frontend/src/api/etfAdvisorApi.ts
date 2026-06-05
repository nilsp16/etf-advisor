// Central API layer — all backend calls live here so components stay clean.
// Uses relative /api URLs; the Vite dev proxy forwards them to Spring Boot.

import type {
  Etf,
  EtfInput,
  Recommendation,
  WatchlistEntry,
  WatchlistInput,
  MarketData,
} from '../types'

const BASE = '/api'

async function handle<T>(response: Response): Promise<T> {
  if (!response.ok) {
    // Backend sends RFC-7807 ProblemDetail; try to surface its message.
    let message = `HTTP ${response.status}`
    try {
      const problem = await response.json()
      if (problem?.detail) message = problem.detail
    } catch {
      // no JSON body, keep default message
    }
    throw new Error(message)
  }
  if (response.status === 204) {
    return undefined as T
  }
  return (await response.json()) as T
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
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  }).then((r) => handle<Etf>(r))
}

export function updateEtf(id: number, input: EtfInput): Promise<Etf> {
  return fetch(`${BASE}/etfs/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  }).then((r) => handle<Etf>(r))
}

export function deleteEtf(id: number): Promise<void> {
  return fetch(`${BASE}/etfs/${id}`, { method: 'DELETE' }).then((r) =>
    handle<void>(r),
  )
}

// --- Market data (Alpaca via backend) ---

export function getMarketData(etfId: number): Promise<MarketData> {
  return fetch(`${BASE}/market-data/${etfId}`).then((r) => handle<MarketData>(r))
}

// --- Recommendations ---

export function getRecommendations(): Promise<Recommendation[]> {
  return fetch(`${BASE}/recommendations`).then((r) =>
    handle<Recommendation[]>(r),
  )
}

export function getRecommendationsForEtf(
  etfId: number,
): Promise<Recommendation[]> {
  return fetch(`${BASE}/recommendations/etf/${etfId}`).then((r) =>
    handle<Recommendation[]>(r),
  )
}

export function generateRecommendation(etfId: number): Promise<Recommendation> {
  return fetch(`${BASE}/recommendations/generate/${etfId}`, {
    method: 'POST',
  }).then((r) => handle<Recommendation>(r))
}

// --- Watchlist ---

export function getWatchlist(): Promise<WatchlistEntry[]> {
  return fetch(`${BASE}/watchlist`).then((r) => handle<WatchlistEntry[]>(r))
}

export function addToWatchlist(input: WatchlistInput): Promise<WatchlistEntry> {
  return fetch(`${BASE}/watchlist`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  }).then((r) => handle<WatchlistEntry>(r))
}

export function removeFromWatchlist(id: number): Promise<void> {
  return fetch(`${BASE}/watchlist/${id}`, { method: 'DELETE' }).then((r) =>
    handle<void>(r),
  )
}
