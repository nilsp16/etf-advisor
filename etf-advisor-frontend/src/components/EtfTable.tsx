// ETF list table with latest recommendation signal shown as colored badge per row.

import { useEffect, useState } from 'react'
import type { Etf, Recommendation } from '../types'
import { getRecommendationsForEtf } from '../api/etfAdvisorApi'
import SignalBadge from './SignalBadge'

type Props = {
  etfs: Etf[]
  selectedId: number | null
  onSelect: (id: number) => void
}

export default function EtfTable({ etfs, selectedId, onSelect }: Props) {
  const [latestRecs, setLatestRecs] = useState<Record<number, Recommendation | null>>({})

  useEffect(() => {
    etfs.forEach((etf) => {
      getRecommendationsForEtf(etf.id)
          .then((list) => {
            const latest = list.length > 0 ? list[list.length - 1] : null
            setLatestRecs((prev) => ({ ...prev, [etf.id]: latest }))
          })
          .catch(() => {
            setLatestRecs((prev) => ({ ...prev, [etf.id]: null }))
          })
    })
  }, [etfs])

  return (
      <div className="card">
        <h2>Markets</h2>
        <table className="table">
          <thead>
          <tr>
            <th>Signal</th>
            <th>Name</th>
            <th>Ticker</th>
            <th className="num">Price</th>
            <th className="num">TER</th>
            <th>ISIN</th>
          </tr>
          </thead>
          <tbody>
          {etfs.map((etf) => {
            const rec = latestRecs[etf.id]
            return (
                <tr
                    key={etf.id}
                    className={etf.id === selectedId ? 'row--selected' : ''}
                    onClick={() => onSelect(etf.id)}
                >
                  <td>
                    {rec ? (
                        <SignalBadge signal={rec.signal} />
                    ) : (
                        <span className="muted small">—</span>
                    )}
                  </td>
                  <td>{etf.name}</td>
                  <td className="mono">{etf.ticker}</td>
                  <td className="num mono">${etf.currentPrice.toFixed(2)}</td>
                  <td className="num mono">{etf.ter ? `${etf.ter}%` : '—'}</td>
                  <td className="mono small">{etf.isin || '—'}</td>
                </tr>
            )
          })}
          </tbody>
        </table>
      </div>
  )
}