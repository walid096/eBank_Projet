import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import Forbidden from '../pages/Forbidden';

// Role guard: Requires specific role (CLIENT or AGENT_GUICHET)
// Matches backend @PreAuthorize("hasRole('CLIENT')") or @PreAuthorize("hasRole('AGENT_GUICHET')")
const RoleGuard = ({ children, requiredRole }) => {
    const { isAuthenticated, hasRole } = useAuth();

    if (!isAuthenticated()) {
        return <Navigate to="/login" replace />;
    }

    if (!hasRole(requiredRole)) {
        // Show forbidden page (matches RestAccessDeniedHandler message)
        return <Forbidden />;
    }

    return children;
};

export default RoleGuard;