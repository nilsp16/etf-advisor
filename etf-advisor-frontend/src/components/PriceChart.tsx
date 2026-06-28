// Price chart built from recommendation history. Shows price_at_generation
// over time as an SVG line chart with day/week/year timeframe selector.

import { useMemo, useState } from 'react'
import type { Recommendation } from '../types'

type Timeframe = '1D' | '1W' | '1Y'

type Props = {
    recommendations: Recommendation[]
}

const TIMEFRAME_MS: Record<Timeframe, number> = {
    '1D': 24 * 60 * 60 * 1000,
    '1W': 7 * 24 * 60 * 60 * 1000,
    '1Y': 365 * 24 * 60 * 60 * 1000,
}

export default function PriceChart({ recommendations }: Props) {
    const [timeframe, setTimeframe] = useState<Timeframe>('1W')

    const filtered = useMemo(() => {
        const cutoff = Date.now() - TIMEFRAME_MS[timeframe]
        return recommendations
            .filter((r) => new Date(r.generatedAt).getTime() > cutoff)
            .sort((a, b) => new Date(a.generatedAt).getTime() - new Date(b.generatedAt).getTime())
    }, [recommendations, timeframe])

    if (filtered.length < 2) {
        return (
            <div className="chart-container">
                <div className="chart-header">
                    <h3>Price History</h3>
                    <div className="timeframe-selector">
                        {(['1D', '1W', '1Y'] as Timeframe[]).map((tf) => (
                            <button
                                key={tf}
                                className={`tf-btn ${tf === timeframe ? 'tf-btn--active' : ''}`}
                                onClick={() => setTimeframe(tf)}
                            >
                                {tf}
                            </button>
                        ))}
                    </div>
                </div>
                <p className="muted small" style={{ textAlign: 'center', padding: '2rem 0' }}>
                    Not enough data points for {timeframe} chart. Recommendations are generated every 60s.
                </p>
            </div>
        )
    }

    const prices = filtered.map((r) => r.priceAtGeneration)
    const times = filtered.map((r) => new Date(r.generatedAt).getTime())
    const minPrice = Math.min(...prices)
    const maxPrice = Math.max(...prices)
    const minTime = times[0]
    const maxTime = times[times.length - 1]
    const priceRange = maxPrice - minPrice || 1
    const timeRange = maxTime - minTime || 1

    const W = 600
    const H = 200
    const PAD_X = 50
    const PAD_Y = 20

    function x(t: number) {
        return PAD_X + ((t - minTime) / timeRange) * (W - PAD_X - 10)
    }
    function y(p: number) {
        return PAD_Y + (1 - (p - minPrice) / priceRange) * (H - PAD_Y * 2)
    }

    const points = filtered.map((_, i) => `${x(times[i])},${y(prices[i])}`).join(' ')

    // Determine trend color
    const firstPrice = prices[0]
    const lastPrice = prices[prices.length - 1]
    const trendUp = lastPrice >= firstPrice
    const lineColor = trendUp ? 'var(--buy)' : 'var(--sell)'
    const fillColor = trendUp ? 'var(--buy-bg)' : 'var(--sell-bg)'

    // Area fill path
    const areaPath = `M${x(times[0])},${y(prices[0])} ${filtered
        .map((_, i) => `L${x(times[i])},${y(prices[i])}`)
        .join(' ')} L${x(times[times.length - 1])},${H - PAD_Y} L${x(times[0])},${H - PAD_Y} Z`

    // Y-axis labels
    const yLabels = [minPrice, minPrice + priceRange / 2, maxPrice]

    // X-axis labels
    const formatTime = (t: number) => {
        const d = new Date(t)
        if (timeframe === '1D') return d.toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' })
        return d.toLocaleDateString('en-GB', { day: '2-digit', month: 'short' })
    }

    const change = lastPrice - firstPrice
    const changePct = ((change / firstPrice) * 100).toFixed(2)

    return (
        <div className="chart-container">
            <div className="chart-header">
                <div>
                    <h3>Price History</h3>
                    <span className={`chart-change mono ${trendUp ? 'chart-change--up' : 'chart-change--down'}`}>
            {trendUp ? '▲' : '▼'} ${Math.abs(change).toFixed(2)} ({trendUp ? '+' : ''}{changePct}%)
          </span>
                </div>
                <div className="timeframe-selector">
                    {(['1D', '1W', '1Y'] as Timeframe[]).map((tf) => (
                        <button
                            key={tf}
                            className={`tf-btn ${tf === timeframe ? 'tf-btn--active' : ''}`}
                            onClick={() => setTimeframe(tf)}
                        >
                            {tf}
                        </button>
                    ))}
                </div>
            </div>

            <svg viewBox={`0 0 ${W} ${H}`} className="chart-svg">
                {/* Grid lines */}
                {yLabels.map((p) => (
                    <line
                        key={p}
                        x1={PAD_X}
                        y1={y(p)}
                        x2={W - 10}
                        y2={y(p)}
                        stroke="var(--border)"
                        strokeDasharray="4 4"
                    />
                ))}

                {/* Y labels */}
                {yLabels.map((p) => (
                    <text key={`l-${p}`} x={PAD_X - 5} y={y(p) + 4} textAnchor="end" className="chart-label">
                        ${p.toFixed(1)}
                    </text>
                ))}

                {/* X labels */}
                <text x={x(minTime)} y={H - 4} textAnchor="start" className="chart-label">
                    {formatTime(minTime)}
                </text>
                <text x={x(maxTime)} y={H - 4} textAnchor="end" className="chart-label">
                    {formatTime(maxTime)}
                </text>

                {/* Area fill */}
                <path d={areaPath} fill={fillColor} opacity="0.3" />

                {/* Line */}
                <polyline points={points} fill="none" stroke={lineColor} strokeWidth="2" />

                {/* Dots at start and end */}
                <circle cx={x(times[0])} cy={y(prices[0])} r="3" fill={lineColor} />
                <circle
                    cx={x(times[times.length - 1])}
                    cy={y(prices[prices.length - 1])}
                    r="4"
                    fill={lineColor}
                    stroke="var(--surface)"
                    strokeWidth="2"
                />
            </svg>
        </div>
    )
}