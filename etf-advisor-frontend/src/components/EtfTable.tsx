// List view: shows all ETFs in a table and reports selection upward.

import type { Etf } from '../types'

type Props = {
  etfs: Etf[]
  selectedId: number | null
  onSelect: (id: number) => void
}

export default function EtfTable({ etfs, selectedId, onSelect }: Props) {
  return (
    <div className="card">
      <table className="table">
        <thead>
          <tr>
            <th>Ticker</th>
            <th>Name</th>
            <th className="num">Price</th>
            <th className="num">TER</th>
            <th>Policy</th>
          </tr>
        </thead>
        <tbody>
          {etfs.map((etf) => (
            <tr
              key={etf.id}
              className={etf.id === selectedId ? 'row--selected' : ''}
              onClick={() => onSelect(etf.id)}
            >
              <td className="mono">{etf.ticker}</td>
              <td>{etf.name}</td>
              <td className="num mono">
                {etf.currentPrice.toLocaleString('en-US', {
                  style: 'currency',
                  currency: etf.currency,
                })}
              </td>
              <td className="num mono">{etf.ter.toFixed(2)}%</td>
              <td>{etf.dividendPolicy}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
