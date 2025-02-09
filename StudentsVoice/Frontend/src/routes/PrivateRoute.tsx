import React from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import {useUser} from '../context/UserContext';

export const RequireAuth: React.FC<{ allowedRoles?: string[] }> = ({ allowedRoles }) => {
    const { user } = useUser();

    if (!user) {
        return <Navigate to="/login" />;
    }

    if (allowedRoles && !allowedRoles.some(role => user.roles.includes(role))) {
        return <Navigate to="/unauthorized" />;
    }

    return <Outlet />;
};

export const RedirectIfAuthenticated: React.FC = () => {
    const { user } = useUser();

    return user ? <Navigate to="/dashboard" replace /> : <Outlet />;
};

