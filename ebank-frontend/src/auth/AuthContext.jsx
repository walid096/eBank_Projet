import React, { createContext, useContext, useState, useEffect } from 'react';
import { tokenStorage } from './tokenStorage';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState({
        token: tokenStorage.getToken(),
        role: tokenStorage.getRole(),
    });

    // Initialize from localStorage on mount
    useEffect(() => {
        const token = tokenStorage.getToken();
        const role = tokenStorage.getRole();
        if (token && role) {
            setUser({ token, role });
        }
    }, []);

    // Login: Save token and role (matches LoginResponse from backend)
    const login = (token, role) => {
        tokenStorage.setToken(token, role);
        setUser({ token, role });
    };

    // Logout: Clear token and role
    const logout = () => {
        tokenStorage.clearToken();
        setUser({ token: null, role: null });
    };

    // Check if user has specific role (matches Role enum)
    const hasRole = (requiredRole) => {
        return user.role === requiredRole;
    };

    // Check if user is authenticated
    const isAuthenticated = () => {
        return !!user.token;
    };

    return (
        <AuthContext.Provider
            value={{
                user,
                login,
                logout,
                hasRole,
                isAuthenticated,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider');
    }
    return context;
};