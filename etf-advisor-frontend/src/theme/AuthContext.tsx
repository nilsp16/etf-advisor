// Auth context — stores JWT token and user info, provides login/register/logout.
// The token is kept in memory (useState) so it survives page navigation but
// clears on refresh — acceptable for a student project.

import { createContext, useContext, useState, useCallback } from 'react'
import type { ReactNode } from 'react'

interface AuthContextValue {
    token: string | null
    username: string | null
    role: string | null
    login: (token: string) => void
    logout: () => void
    isLoggedIn: boolean
    isAdmin: boolean
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

function parseToken(token: string): { sub: string; role: string } | null {
    try {
        const payload = token.split('.')[1]
        const decoded = JSON.parse(atob(payload))
        return { sub: decoded.sub, role: decoded.role }
    } catch {
        return null
    }
}

export function AuthProvider({ children }: { children: ReactNode }) {
    const [token, setToken] = useState<string | null>(null)
    const [username, setUsername] = useState<string | null>(null)
    const [role, setRole] = useState<string | null>(null)

    const login = useCallback((newToken: string) => {
        const info = parseToken(newToken)
        setToken(newToken)
        setUsername(info?.sub ?? null)
        setRole(info?.role ?? null)
    }, [])

    const logout = useCallback(() => {
        setToken(null)
        setUsername(null)
        setRole(null)
    }, [])

    return (
        <AuthContext.Provider
            value={{
                token,
                username,
                role,
                login,
                logout,
                isLoggedIn: token !== null,
                isAdmin: role === 'ADMIN',
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth(): AuthContextValue {
    const ctx = useContext(AuthContext)
    if (!ctx) throw new Error('useAuth must be used within AuthProvider')
    return ctx
}