// Standalone watchlist page. Lists saved entries as cards and allows removal.

import type { WatchlistEntry } from '../types'
import { removeFromWatchlist } from '../api/etfAdvisorApi'

type Props = {
  entries: WatchlistEntry[]
  onChanged: () => void
}

export default function WatchlistView({ entries, onChanged }: Props) {
  async function handleRemove(id: number) {
    try {
      await removeFromWatchlist(id)
      onChanged()
    } catch {
      onChanged()
    }
  }

  if (entries.length === 0) {
    return (
      <div className="card">
        <h2>Watchlist</h2>
        <p className="muted">
          Your watchlist is empty. Open an ETF and add it from the detail view.
        </p>
      </div>
    )
  }

  return (
    <div className="card">
      <h2>Watchlist</h2>
      <p className="muted small">{entries.length} saved ETF(s)</p>
      <div className="watchlist-grid">
        {entries.map((entry) => (
          <div key={entry.id} className="watchlist-card">
            <div className="watchlist-card__head">
              <strong>{entry.etfName}</strong>
              <button className="ghost small-btn" onClick={() => handleRemove(entry.id)}>
                Remove
              </button>
            </div>
            {entry.userNote && <p className="watchlist-card__note">{entry.userNote}</p>}
            <span className="mono muted small">
              Added {new Date(entry.addedAt).toLocaleDateString('en-GB')}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}
