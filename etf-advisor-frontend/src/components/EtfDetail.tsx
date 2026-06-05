// Detail view for the selected ETF. Loads live market data from Alpaca,
// shows the latest recommendation prominently (signal + score), lets the user
// generate a new recommendation and add the ETF to the watchlist.

import { useCallback, useEffect, useState } from 'react'
import type { Etf, MarketData, Recommendation } from '../types'
import {
  getMarketData,
  getRecommendationsForEtf,
  generateRecommendation,
  addToWatchlist,
} from '../api/etfAdvisorApi'
import SignalBadge from './SignalBadge'

type Props = {
  etf: Etf
  onWatchlistChanged: () => void
}

export default function EtfDetail({ etf, onWatchlistChanged }: Props) {
  const [market, setMarket] = useState<MarketData | null>(null)
  const [marketError, setMarketError] = useState<string | null>(null)
  const [recommendations, setRecommendations] = useState<Recommendation[]>([])
  const [busy, setBusy] = useState(false)
  const [note, setNote] = useState('')
  const [message, setMessage] = useState<string | null>(null)

  const loadRecommendations = useCallback(() => {
    getRecommendationsForEtf(etf.id)
      .then(setRecommendations)
      .catch(() => setRecommendations([]))
  }, [etf.id])

  useEffect(() => {
    let ignore = false
    setMarket(null)
    setMarketError(null)
    setMessage(null)

    getMarketData(etf.id)
      .then((data) => {
        if (!ignore) setMarket(data)
      })
      .catch((err) => {
        if (!ignore)
          setMarketError(err instanceof Error ? err.message : 'Error')
      })

    loadRecommendations()
    return () => {
      ignore = true
    }
  }, [etf.id, loadRecommendations])

  async function handleGenerate() {
    setBusy(true)
    setMessage(null)
    try {
      await generateRecommendation(etf.id)
      loadRecommendations()
    } catch (err) {
      setMessage(err instanceof Error ? err.message : 'Error')
    } finally {
      setBusy(false)
    }
  }

  async function handleAddToWatchlist() {
    setBusy(true)
    setMessage(null)
    try {
      await addToWatchlist({ etfId: etf.id, userNote: note })
      setNote('')
      onWatchlistChanged()
      setMessage('Added to watchlist.')
    } catch (err) {
      setMessage(err instanceof Error ? err.message : 'Error')
    } finally {
      setBusy(false)
    }
  }

  // The most recent recommendation drives the headline signal + score.
  const latest = recommendations.length > 0 ? recommendations[recommendations.length - 1] : null

  return (
    <div className="card detail">
      <div className="detail__head">
        <div>
          <span className="mono detail__ticker">{etf.ticker}</span>
          <h2>{etf.name}</h2>
          <p className="muted mono">{etf.isin}</p>
        </div>
        {latest && (
          <div className={`verdict verdict--${latest.signal.toLowerCase()}`}>
            <SignalBadge signal={latest.signal} size="lg" />
            <div className="verdict__score">
              <span className="field__label">Score</span>
              <span className="verdict__score-value mono">
                {latest.score > 0 ? `+${latest.score}` : latest.score}
              </span>
            </div>
          </div>
        )}
      </div>

      <div className="grid">
        <Field label="Currency" value={etf.currency} />
        <Field label="TER" value={`${etf.ter.toFixed(2)}%`} />
        <Field label="Dividend" value={etf.dividendPolicy} />
        <Field label="Replication" value={etf.replicationMethod} />
        <Field label="52W Low" value={etf.low52Week.toFixed(2)} />
        <Field label="52W High" value={etf.high52Week.toFixed(2)} />
      </div>

      <h3>Live Market Data</h3>
      {marketError && <p className="error">Could not load market data: {marketError}</p>}
      {!market && !marketError && <p className="muted">Loading…</p>}
      {market && (
        <div className="grid">
          <Field label="Price" value={market.currentPrice.toFixed(2)} accent />
          <Field label="Open" value={market.open.toFixed(2)} />
          <Field label="High" value={market.high.toFixed(2)} />
          <Field label="Low" value={market.low.toFixed(2)} />
          <Field label="Volume" value={market.volume.toLocaleString('en-US')} />
        </div>
      )}

      <div className="actions">
        <button onClick={handleGenerate} disabled={busy}>
          {busy ? 'Working…' : 'Generate Recommendation'}
        </button>
        <div className="watchlist-add">
          <input
            placeholder="Note (optional)"
            value={note}
            onChange={(e) => setNote(e.target.value)}
          />
          <button onClick={handleAddToWatchlist} disabled={busy}>
            Add to Watchlist
          </button>
        </div>
      </div>
      {message && <p className="muted">{message}</p>}

      <h3>Recommendation History</h3>
      {recommendations.length === 0 ? (
        <p className="muted">No recommendations yet — generate one above.</p>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>Signal</th>
              <th className="num">Score</th>
              <th className="num">Price</th>
              <th>Generated</th>
            </tr>
          </thead>
          <tbody>
            {[...recommendations].reverse().map((r) => (
              <tr key={r.id}>
                <td>
                  <SignalBadge signal={r.signal} />
                </td>
                <td className="num mono">{r.score > 0 ? `+${r.score}` : r.score}</td>
                <td className="num mono">{r.priceAtGeneration.toFixed(2)}</td>
                <td className="mono muted">
                  {new Date(r.generatedAt).toLocaleString('en-GB')}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

function Field({
  label,
  value,
  accent,
}: {
  label: string
  value: string
  accent?: boolean
}) {
  return (
    <div className="field">
      <span className="field__label">{label}</span>
      <span className={`field__value mono ${accent ? 'field__value--accent' : ''}`}>
        {value}
      </span>
    </div>
  )
}
