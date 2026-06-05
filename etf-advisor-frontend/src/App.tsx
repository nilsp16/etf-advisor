// Root component. Holds shared state (etfs, selection, watchlist) and switches
// between two views: the markets dashboard (list + detail) and the watchlist.

import { useCallback, useEffect, useState } from 'react'
import type { Etf, WatchlistEntry } from './types'
import { getEtfs, getWatchlist } from './api/etfAdvisorApi'
import { useTheme } from './theme/ThemeContext'
import EtfTable from './components/EtfTable'
import EtfDetail from './components/EtfDetail'
import EtfForm from './components/EtfForm'
import WatchlistView from './components/WatchlistView'
import './App.css'

type View = 'markets' | 'watchlist'

export default function App() {
  const { theme, toggle } = useTheme()
  const [view, setView] = useState<View>('markets')
  const [etfs, setEtfs] = useState<Etf[]>([])
  const [watchlist, setWatchlist] = useState<WatchlistEntry[]>([])
  const [selectedId, setSelectedId] = useState<number | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showForm, setShowForm] = useState(false)

  const loadEtfs = useCallback(() => {
    setLoading(true)
    getEtfs()
      .then((data) => {
        setEtfs(data)
        setError(null)
      })
      .catch((err) => setError(err instanceof Error ? err.message : 'Error'))
      .finally(() => setLoading(false))
  }, [])

  const loadWatchlist = useCallback(() => {
    getWatchlist()
      .then(setWatchlist)
      .catch(() => setWatchlist([]))
  }, [])

  useEffect(() => {
    loadEtfs()
    loadWatchlist()
  }, [loadEtfs, loadWatchlist])

  const selected = etfs.find((e) => e.id === selectedId) ?? null

  return (
    <div className="app">
      <header className="header">
        <div className="brand">
          <span className="brand__mark">◆</span>
          <div>
            <h1>ETF Advisor</h1>
            <p className="muted small">Market data &amp; signals</p>
          </div>
        </div>

        <nav className="nav">
          <button
            className={`nav__item ${view === 'markets' ? 'nav__item--active' : ''}`}
            onClick={() => setView('markets')}
          >
            Markets
          </button>
          <button
            className={`nav__item ${view === 'watchlist' ? 'nav__item--active' : ''}`}
            onClick={() => setView('watchlist')}
          >
            Watchlist
            {watchlist.length > 0 && <span className="nav__count">{watchlist.length}</span>}
          </button>
        </nav>

        <div className="header__actions">
          {view === 'markets' && (
            <button className="ghost" onClick={() => setShowForm((s) => !s)}>
              {showForm ? 'Close' : 'Add ETF'}
            </button>
          )}
          <button className="ghost icon" onClick={toggle} aria-label="Toggle theme">
            {theme === 'dark' ? '☀' : '☾'}
          </button>
        </div>
      </header>

      {loading && <p className="muted">Loading…</p>}
      {error && <p className="error">Failed to load: {error}</p>}

      {!loading && !error && view === 'markets' && (
        <>
          {showForm && (
            <EtfForm
              onCreated={() => {
                loadEtfs()
                setShowForm(false)
              }}
            />
          )}
          <main className="layout-single">
            <EtfTable etfs={etfs} selectedId={selectedId} onSelect={setSelectedId} />
            {selected ? (
              <EtfDetail etf={selected} onWatchlistChanged={loadWatchlist} />
            ) : (
              <div className="card placeholder muted">
                Select an ETF to see details, live market data and recommendations.
              </div>
            )}
          </main>
        </>
      )}

      {!loading && !error && view === 'watchlist' && (
        <main className="layout-single">
          <WatchlistView entries={watchlist} onChanged={loadWatchlist} />
        </main>
      )}
    </div>
  )
}
