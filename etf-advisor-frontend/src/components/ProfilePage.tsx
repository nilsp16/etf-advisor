// Profile page — shows username, role, and logout button.

import { useAuth } from '../theme/AuthContext'
import { setApiToken } from '../api/etfAdvisorApi'

export default function ProfilePage() {
    const auth = useAuth()

    function handleLogout() {
        setApiToken(null)
        auth.logout()
    }

    return (
        <div className="card profile">
            <h2>Profile</h2>
            <div className="grid">
                <div className="field">
                    <span className="field__label">Username</span>
                    <span className="field__value mono">{auth.username}</span>
                </div>
                <div className="field">
                    <span className="field__label">Role</span>
                    <span className={`field__value mono ${auth.isAdmin ? 'role-admin' : 'role-user'}`}>
            {auth.role}
          </span>
                </div>
            </div>
            <div className="actions" style={{ marginTop: '2rem' }}>
                <button onClick={handleLogout}>Sign Out</button>
            </div>
        </div>
    )
}
