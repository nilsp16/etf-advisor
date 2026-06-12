// Admin view — lists all ETFs and allows editing metadata fields.
// Only accessible to ADMIN users.

import { useEffect, useState } from 'react'
import type { Etf, EtfInput } from '../types'
import { getEtfs, updateEtf, deleteEtf } from '../api/etfAdvisorApi'

export default function AdminPanel({ onEtfsChanged }: { onEtfsChanged: () => void }) {
    const [etfs, setEtfs] = useState<Etf[]>([])
    const [editingId, setEditingId] = useState<number | null>(null)
    const [form, setForm] = useState<EtfInput | null>(null)
    const [message, setMessage] = useState<string | null>(null)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        loadEtfs()
    }, [])

    function loadEtfs() {
        getEtfs().then(setEtfs).catch(() => setEtfs([]))
    }

    function startEdit(etf: Etf) {
        setEditingId(etf.id)
        setForm({
            name: etf.name || '',
            isin: etf.isin || '',
            ticker: etf.ticker || '',
            currency: etf.currency || 'USD',
            ter: etf.ter || 0,
            currentPrice: etf.currentPrice || 0,
            dividendPolicy: etf.dividendPolicy || 'Unknown',
            low52Week: etf.low52Week || 0,
            high52Week: etf.high52Week || 0,
            marketCap: etf.marketCap || 0,
            replicationMethod: etf.replicationMethod || 'Unknown',
        })
        setMessage(null)
        setError(null)
    }

    function cancelEdit() {
        setEditingId(null)
        setForm(null)
    }

    function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
        if (!form) return
        const { name, value, type } = e.target
        setForm((prev) => prev ? { ...prev, [name]: type === 'number' ? Number(value) : value } : prev)
    }

    async function handleSave() {
        if (!form || editingId === null) return
        setError(null)
        try {
            await updateEtf(editingId, form)
            setMessage('Saved.')
            setEditingId(null)
            setForm(null)
            loadEtfs()
            onEtfsChanged()
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Error')
        }
    }

    async function handleDelete(id: number) {
        setError(null)
        try {
            await deleteEtf(id)
            loadEtfs()
            onEtfsChanged()
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Error')
        }
    }

    return (
        <div className="card">
            <h2>Admin — ETF Metadata</h2>
            <p className="muted small">Edit metadata fields that cannot be fetched from external APIs.</p>
            {message && <p className="muted">{message}</p>}
            {error && <p className="error">{error}</p>}

            <table className="table">
                <thead>
                <tr>
                    <th>Ticker</th>
                    <th>Name</th>
                    <th>ISIN</th>
                    <th className="num">TER</th>
                    <th>Policy</th>
                    <th>Replication</th>
                    <th className="num">Market Cap</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {etfs.map((etf) =>
                    editingId === etf.id && form ? (
                        <tr key={etf.id} className="row--editing">
                            <td className="mono">{etf.ticker}</td>
                            <td>
                                <input name="name" value={form.name} onChange={handleChange} className="inline-input" />
                            </td>
                            <td>
                                <input name="isin" value={form.isin} onChange={handleChange} className="inline-input" />
                            </td>
                            <td>
                                <input type="number" step="0.01" name="ter" value={form.ter} onChange={handleChange} className="inline-input num" />
                            </td>
                            <td>
                                <select name="dividendPolicy" value={form.dividendPolicy} onChange={handleChange} className="inline-input">
                                    <option>Accumulating</option>
                                    <option>Distributing</option>
                                    <option>Unknown</option>
                                </select>
                            </td>
                            <td>
                                <select name="replicationMethod" value={form.replicationMethod} onChange={handleChange} className="inline-input">
                                    <option>Physical</option>
                                    <option>Synthetic</option>
                                    <option>Unknown</option>
                                </select>
                            </td>
                            <td>
                                <input type="number" step="0.1" name="marketCap" value={form.marketCap} onChange={handleChange} className="inline-input num" />
                            </td>
                            <td className="action-cell">
                                <button className="ghost small-btn" onClick={handleSave}>Save</button>
                                <button className="ghost small-btn" onClick={cancelEdit}>Cancel</button>
                            </td>
                        </tr>
                    ) : (
                        <tr key={etf.id}>
                            <td className="mono">{etf.ticker}</td>
                            <td>{etf.name || <span className="muted">—</span>}</td>
                            <td className="mono">{etf.isin || <span className="muted">—</span>}</td>
                            <td className="num mono">{etf.ter ? `${etf.ter.toFixed(2)}%` : <span className="muted">—</span>}</td>
                            <td>{etf.dividendPolicy || <span className="muted">—</span>}</td>
                            <td>{etf.replicationMethod || <span className="muted">—</span>}</td>
                            <td className="num mono">{etf.marketCap || <span className="muted">—</span>}</td>
                            <td className="action-cell">
                                <button className="ghost small-btn" onClick={() => startEdit(etf)}>Edit</button>
                                <button className="ghost small-btn danger-btn" onClick={() => handleDelete(etf.id)}>Delete</button>
                            </td>
                        </tr>
                    ),
                )}
                </tbody>
            </table>
        </div>
    )
}