// Watchlist view with filter/sort bar, recommendation banners, and add-by-ticker form.

import { useEffect, useMemo, useState } from 'react'
import type { WatchlistEntry, Recommendation, Signal } from '../types'
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

type SortOption = 'name' | 'date' | 'signal'
type FilterSignal = 'ALL' | Signal

export default function WatchlistView({ entries, onChanged }: Props) {
    const [recs, setRecs] = useState<Record<number, Recommendation | null>>({})
    const [ticker, setTicker] = useState('')
    const [note, setNote] = useState('')
    const [busy, setBusy] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [sortBy, setSortBy] = useState<SortOption>('date')
    const [filterSignal, setFilterSignal] = useState<FilterSignal>('ALL')

    useEffect(() => {
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

    // Filter and sort
    const displayed = useMemo(() => {
        let result = [...entries]

        // Filter by signal
        if (filterSignal !== 'ALL') {
            result = result.filter((e) => recs[e.etfId]?.signal === filterSignal)
        }

        // Sort
        result.sort((a, b) => {
            if (sortBy === 'name') return a.etfName.localeCompare(b.etfName)
            if (sortBy === 'date') return new Date(b.addedAt).getTime() - new Date(a.addedAt).getTime()
            if (sortBy === 'signal') {
                const order: Record<string, number> = { BUY: 0, HOLD: 1, SELL: 2 }
                const sa = recs[a.etfId]?.signal ?? 'HOLD'
                const sb = recs[b.etfId]?.signal ?? 'HOLD'
                return (order[sa] ?? 3) - (order[sb] ?? 3)
            }
            return 0
        })

        return result
    }, [entries, recs, sortBy, filterSignal])

    async function handleRemove(id: number) {
        try {
            await removeFromWatchlist(id)
            onChanged()
        } catch {
            onChanged()
        }
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

    // Count signals for filter badges
    const signalCounts = useMemo(() => {
        const counts = { BUY: 0, HOLD: 0, SELL: 0 }
        entries.forEach((e) => {
            const sig = recs[e.etfId]?.signal
            if (sig) counts[sig]++
        })
        return counts
    }, [entries, recs])

    return (
        <div className="card">
            <h2>Watchlist</h2>

            {/* Add by ticker */}
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

            {/* Filter & Sort bar */}
            {entries.length > 0 && (
                <div className="watchlist-controls">
                    <div className="filter-bar">
                        <span className="muted small">Filter:</span>
                        <button
                            className={`filter-btn ${filterSignal === 'ALL' ? 'filter-btn--active' : ''}`}
                            onClick={() => setFilterSignal('ALL')}
                        >
                            All ({entries.length})
                        </button>
                        <button
                            className={`filter-btn filter-btn--buy ${filterSignal === 'BUY' ? 'filter-btn--active' : ''}`}
                            onClick={() => setFilterSignal('BUY')}
                        >
                            BUY ({signalCounts.BUY})
                        </button>
                        <button
                            className={`filter-btn filter-btn--hold ${filterSignal === 'HOLD' ? 'filter-btn--active' : ''}`}
                            onClick={() => setFilterSignal('HOLD')}
                        >
                            HOLD ({signalCounts.HOLD})
                        </button>
                        <button
                            className={`filter-btn filter-btn--sell ${filterSignal === 'SELL' ? 'filter-btn--active' : ''}`}
                            onClick={() => setFilterSignal('SELL')}
                        >
                            SELL ({signalCounts.SELL})
                        </button>
                    </div>
                    <div className="sort-bar">
                        <span className="muted small">Sort:</span>
                        <button
                            className={`filter-btn ${sortBy === 'date' ? 'filter-btn--active' : ''}`}
                            onClick={() => setSortBy('date')}
                        >
                            Date
                        </button>
                        <button
                            className={`filter-btn ${sortBy === 'name' ? 'filter-btn--active' : ''}`}
                            onClick={() => setSortBy('name')}
                        >
                            Name
                        </button>
                        <button
                            className={`filter-btn ${sortBy === 'signal' ? 'filter-btn--active' : ''}`}
                            onClick={() => setSortBy('signal')}
                        >
                            Signal
                        </button>
                    </div>
                </div>
            )}

            {displayed.length === 0 && entries.length > 0 && (
                <p className="muted" style={{ marginTop: '1rem' }}>
                    No entries match the current filter.
                </p>
            )}

            {entries.length === 0 ? (
                <p className="muted" style={{ marginTop: '1.5rem' }}>
                    Your watchlist is empty. Add an ETF by ticker above or from the detail view.
                </p>
            ) : (
                <div className="watchlist-grid">
                    {displayed.map((entry) => {
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