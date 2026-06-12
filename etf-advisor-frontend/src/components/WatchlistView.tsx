// Watchlist view — shows saved entries as cards with the latest recommendation
// signal as a colored banner. Includes add-by-ticker form.

import { useEffect, useState } from 'react'
import type { WatchlistEntry, Recommendation } from '../types'
import {
    removeFromWatchlist,
    addToWatchlistByTicker,
    getRecommendationsForEtf,
} from '../api/etfAdvisorApi'
import SignalBadge from './SignalBadge'

type Props = {
    entries: WatchlistEntry[]
    onChanged: () => void
}

export default function WatchlistView({ entries, onChanged }: Props) {
    const [recs, setRecs] = useState<Record<number, Recommendation | null>>({})
    const [ticker, setTicker] = useState('')
    const [note, setNote] = useState('')
    const [busy, setBusy] = useState(false)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        // Load latest recommendation for each unique ETF in the watchlist
        const etfIds = [...new Set(entries.map((e) => e.etfId))]
        etfIds.forEach((etfId) => {
            getRecommendationsForEtf(etfId)
                .then((list) => {
                    const latest = list.length > 0 ? list[list.length - 1] : null
                    setRecs((prev) => ({ ...prev, [etfId]: latest }))
                })
                .catch(() => {
                    setRecs((prev) => ({ ...prev, [etfId]: null }))
                })
        })
    }, [entries])

    async function handleRemove(id: number) {
        try {
            await removeFromWatchlist(id)
            onChanged()
        } catch { onChanged() }
    }

    async function handleAddByTicker() {
        if (!ticker.trim()) return
        setBusy(true)
        setError(null)
        try {
            await addToWatchlistByTicker(ticker.trim().toUpperCase(), note)
            setTicker('')
            setNote('')
            onChanged()
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Error')
        } finally {
            setBusy(false)
        }
    }

    return (
        <div className="card">
            <h2>Watchlist</h2>

            <div className="add-ticker-form">
                <input
                    placeholder="Ticker (e.g. SPY)"
                    value={ticker}
                    onChange={(e) => setTicker(e.target.value)}
                    className="ticker-input"
                />
                <input
                    placeholder="Note (optional)"
                    value={note}
                    onChange={(e) => setNote(e.target.value)}
                />
                <button onClick={handleAddByTicker} disabled={busy}>
                    {busy ? 'Adding…' : 'Add by Ticker'}
                </button>
            </div>
            {error && <p className="error">{error}</p>}

            {entries.length === 0 ? (
                <p className="muted" style={{ marginTop: '1.5rem' }}>
                    Your watchlist is empty. Add an ETF by ticker above or from the detail view.
                </p>
            ) : (
                <div className="watchlist-grid">
                    {entries.map((entry) => {
                        const rec = recs[entry.etfId]
                        return (
                            <div
                                key={entry.id}
                                className={`watchlist-card ${rec ? `watchlist-card--${rec.signal.toLowerCase()}` : ''}`}
                            >
                                {rec && (
                                    <div className="watchlist-card__banner">
                                        <SignalBadge signal={rec.signal} />
                                        <span className="mono small">
                      Score: {rec.score > 0 ? `+${rec.score}` : rec.score}
                    </span>
                                    </div>
                                )}
                                <div className="watchlist-card__head">
                                    <strong>{entry.etfName}</strong>
                                    <button className="ghost small-btn" onClick={() => handleRemove(entry.id)}>
                                        Remove
                                    </button>
                                </div>
                                {entry.userNote && <p className="watchlist-card__note">{entry.userNote}</p>}
                                {rec && (
                                    <p className="watchlist-card__reasoning muted small">{rec.reasoning}</p>
                                )}
                                <span className="mono muted small">
                  Added {new Date(entry.addedAt).toLocaleDateString('en-GB')}
                </span>
                            </div>
                        )
                    })}
                </div>
            )}
        </div>
    )
}