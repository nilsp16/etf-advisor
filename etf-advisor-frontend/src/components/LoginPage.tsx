// Login and Register page. Switches between login and register form.
// On success, calls auth.login(token) which stores the JWT.

import { useState } from 'react'
import { useAuth } from '../theme/AuthContext'
import { login as apiLogin, register as apiRegister, setApiToken } from '../api/etfAdvisorApi'

export default function LoginPage() {
    const auth = useAuth()
    const [isRegister, setIsRegister] = useState(false)
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [role, setRole] = useState('USER')
    const [error, setError] = useState<string | null>(null)
    const [busy, setBusy] = useState(false)

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setBusy(true)
        setError(null)
        try {
            const res = isRegister
                ? await apiRegister(username, password, role)
                : await apiLogin(username, password)
            setApiToken(res.token)
            auth.login(res.token)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Authentication failed')
        } finally {
            setBusy(false)
        }
    }

    return (
        <div className="login-page">
            <div className="login-card card">
                <div className="login-header">
                    <span className="brand__mark">◆</span>
                    <h1>ETF Advisor</h1>
                    <p className="muted small">Market data &amp; signals</p>
                </div>

                <h2>{isRegister ? 'Create Account' : 'Sign In'}</h2>
                {error && <p className="error">{error}</p>}

                <div className="login-form" >
                    <label>
                        Username
                        <input
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            autoFocus
                        />
                    </label>
                    <label>
                        Password
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </label>
                    {isRegister && (
                        <label>
                            Role
                            <select value={role} onChange={(e) => setRole(e.target.value)}>
                                <option value="USER">User</option>
                                <option value="ADMIN">Admin</option>
                            </select>
                        </label>
                    )}
                    <button onClick={handleSubmit} disabled={busy}>
                        {busy ? 'Working…' : isRegister ? 'Register' : 'Login'}
                    </button>
                </div>

                <p className="login-switch muted small">
                    {isRegister ? 'Already have an account?' : "Don't have an account?"}{' '}
                    <button className="link-btn" onClick={() => { setIsRegister(!isRegister); setError(null) }}>
                        {isRegister ? 'Sign In' : 'Register'}
                    </button>
                </p>
            </div>
        </div>
    )
}