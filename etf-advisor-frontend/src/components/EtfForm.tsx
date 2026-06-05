// Controlled form to create a new ETF via POST /api/etfs.

import { useState } from 'react'
import type { EtfInput } from '../types'
import { createEtf } from '../api/etfAdvisorApi'

const EMPTY: EtfInput = {
  name: '',
  isin: '',
  ticker: '',
  currency: 'USD',
  ter: 0,
  currentPrice: 0,
  dividendPolicy: 'Accumulating',
  low52Week: 0,
  high52Week: 0,
  marketCap: 0,
  replicationMethod: 'Physical',
}

export default function EtfForm({ onCreated }: { onCreated: () => void }) {
  const [form, setForm] = useState<EtfInput>(EMPTY)
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState<string | null>(null)

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    const { name, value, type } = e.target
    setForm((prev) => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value,
    }))
  }

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setBusy(true)
    setError(null)
    try {
      await createEtf(form)
      setForm(EMPTY)
      onCreated()
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error')
    } finally {
      setBusy(false)
    }
  }

  return (
    <form className="card form" onSubmit={handleSubmit}>
      <h3>Add ETF</h3>
      {error && <p className="error">{error}</p>}
      <div className="form-grid">
        <label>
          Name
          <input name="name" value={form.name} onChange={handleChange} required />
        </label>
        <label>
          ISIN
          <input name="isin" value={form.isin} onChange={handleChange} required />
        </label>
        <label>
          Ticker
          <input name="ticker" value={form.ticker} onChange={handleChange} required />
        </label>
        <label>
          Currency
          <select name="currency" value={form.currency} onChange={handleChange}>
            <option>USD</option>
            <option>EUR</option>
          </select>
        </label>
        <label>
          TER (%)
          <input type="number" step="0.01" name="ter" value={form.ter} onChange={handleChange} />
        </label>
        <label>
          Current Price
          <input type="number" step="0.01" name="currentPrice" value={form.currentPrice} onChange={handleChange} />
        </label>
        <label>
          Dividend Policy
          <select name="dividendPolicy" value={form.dividendPolicy} onChange={handleChange}>
            <option>Accumulating</option>
            <option>Distributing</option>
          </select>
        </label>
        <label>
          Replication
          <select name="replicationMethod" value={form.replicationMethod} onChange={handleChange}>
            <option>Physical</option>
            <option>Synthetic</option>
          </select>
        </label>
        <label>
          52W Low
          <input type="number" step="0.01" name="low52Week" value={form.low52Week} onChange={handleChange} />
        </label>
        <label>
          52W High
          <input type="number" step="0.01" name="high52Week" value={form.high52Week} onChange={handleChange} />
        </label>
        <label>
          Market Cap
          <input type="number" step="0.1" name="marketCap" value={form.marketCap} onChange={handleChange} />
        </label>
      </div>
      <button type="submit" disabled={busy}>
        {busy ? 'Saving…' : 'Create ETF'}
      </button>
    </form>
  )
}
