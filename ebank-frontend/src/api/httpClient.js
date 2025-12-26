import axios from 'axios';

// Base URL matches backend application.properties (server.port=8081)
const BASE_URL = 'http://localhost:8081';

// Create axios instance
const httpClient = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor: Add JWT token to all requests
httpClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor: Handle errors (matches backend error messages)
httpClient.interceptors.response.use(
    (response) => response,
    (error) => {
        // Handle 401 Unauthorized (matches RestAuthenticationEntryPoint)
        if (error.response?.status === 401) {
            const message = error.response.data || 'Session invalide, veuillez s\'authentifier';
            localStorage.removeItem('token');
            localStorage.removeItem('role');
            // Will redirect to login in App.jsx
        }

        // Handle 403 Forbidden (matches RestAccessDeniedHandler)
        if (error.response?.status === 403) {
            const message = error.response.data || 'Vous n\'avez pas le droit d\'accéder à cette fonctionnalité. Veuillez contacter votre administrateur';
            // Will redirect to Forbidden page
        }

        return Promise.reject(error);
    }
);

export default httpClient;