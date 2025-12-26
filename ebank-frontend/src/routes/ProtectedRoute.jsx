import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

// Protected route: Requires authentication (matches backend SecurityConfig)
const ProtectedRoute = ({ children }) => {
    const { isAuthenticated } = useAuth();

    if (!isAuthenticated()) {
        // Redirect to login if not authenticated (matches RestAuthenticationEntryPoint)
        return <Navigate to="/login" replace />;
    }

    return children;
};

export default ProtectedRoute;