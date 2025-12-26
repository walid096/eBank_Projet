// Token storage utilities (matches backend JWT token management)

export const tokenStorage = {
    // Save token and role after login (matches LoginResponse)
    setToken: (token, role) => {
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);
    },

    // Get token for API calls
    getToken: () => {
        return localStorage.getItem('token');
    },

    // Get role for route protection (matches Role enum: CLIENT or AGENT_GUICHET)
    getRole: () => {
        return localStorage.getItem('role');
    },

    // Clear token on logout or token expiration
    clearToken: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
    },

    // Check if user is authenticated
    isAuthenticated: () => {
        return !!localStorage.getItem('token');
    },
};