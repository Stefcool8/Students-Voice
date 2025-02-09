import React from 'react';
import { Link } from 'react-router-dom';
import { logout } from '../api/AuthService.ts';
import { useUser } from '../context/UserContext';
import '../styles/Menu.css';

const Menu: React.FC = () => {
    const { user } = useUser();

    const handleLogout = async () => {
        try {
            await logout();
            window.location.href = '/login'; // Force refresh for logout
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    return (
        <nav className="menu">
            <ul>
                {/* Dashboard: Visible only to logged-in users */}
                {user && (
                    <li>
                        <Link to="/dashboard" className="menu-link">
                            Dashboard
                        </Link>
                    </li>
                )}

                {/* Users: Visible to ADMIN */}
                {user?.roles.includes('ADMIN') && (
                    <li>
                        <Link to="/users" className="menu-link">
                            Manage Users
                        </Link>
                    </li>
                )}

                {/* Evaluations: Redirects based on roles. Visible to logged-in users */}
                {user && (
                    <li>
                        <Link
                            to={
                                user?.roles.includes('ADMIN')
                                    ? '/evaluations'
                                    : user?.roles.includes('TEACHER')
                                        ? '/evaluations-teacher'
                                        : '/evaluations-student'
                            }
                            className="menu-link"
                        >
                            Evaluations
                        </Link>
                    </li>
                )}

                {/* Submit Evaluation: Visible to STUDENT */}
                {user?.roles.includes('STUDENT') && (
                    <li>
                        <Link to="/submit-evaluation" className="menu-link">
                            Submit Evaluation
                        </Link>
                    </li>
                )}

                {/* Logout: Visible to logged-in users */}
                {user && (
                    <li>
                        <button onClick={handleLogout} className="menu-button">
                            Logout
                        </button>
                    </li>
                )}
            </ul>
        </nav>
    );
};

export default Menu;
