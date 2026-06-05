// Reusable signal badge. Filled color highlight: green BUY, yellow HOLD, red SELL.
// `size="lg"` renders a larger version for the detail header.

import type { Signal } from '../types'

type Props = {
  signal: Signal
  size?: 'sm' | 'lg'
}

export default function SignalBadge({ signal, size = 'sm' }: Props) {
  const cls = `signal signal--${signal.toLowerCase()} ${size === 'lg' ? 'signal--lg' : ''}`
  return <span className={cls}>{signal}</span>
}
