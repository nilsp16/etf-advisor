// Domain types — match the backend response DTOs exactly.

export interface Etf {
  id: number
  name: string
  isin: string
  ticker: string
  currency: string
  ter: number
  currentPrice: number
  dividendPolicy: string
  low52Week: number
  high52Week: number
  marketCap: number
  replicationMethod: string
}

export interface EtfInput {
  name: string
  isin: string
  ticker: string
  currency: string
  ter: number
  currentPrice: number
  dividendPolicy: string
  low52Week: number
  high52Week: number
  marketCap: number
  replicationMethod: string
}

export type Signal = 'BUY' | 'HOLD' | 'SELL'

export interface Recommendation {
  id: number
  etfId: number
  etfName: string
  signal: Signal
  reasoning: string
  generatedAt: string
  priceAtGeneration: number
  score: number
}

export interface WatchlistEntry {
  id: number
  etfId: number
  etfName: string
  addedAt: string
  userNote: string | null
}

export interface WatchlistInput {
  etfId: number
  userNote: string
}

export interface MarketData {
  etfId: number
  ticker: string
  currentPrice: number
  open: number
  high: number
  low: number
  close: number
  volume: number
}

// Auth
export interface AuthResponse {
  token: string
}

export interface UserInfo {
  username: string
  role: string
}