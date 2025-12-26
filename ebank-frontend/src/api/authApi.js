import httpClient from './httpClient';

// Matches AuthController endpoints

// POST /api/v1/auth/login
// Request: LoginRequest { login: string, password: string }
// Response: LoginResponse { token: string, expiresInSeconds: number, role: string }
export const login = async (loginData) => {
    const response = await httpClient.post('/api/v1/auth/login', loginData);
    return response.data; // { token, expiresInSeconds, role }
};

// PUT /api/v1/auth/change-password
// Request: ChangePasswordRequest { oldPassword: string, newPassword: string }
// Response: 204 No Content
export const changePassword = async (passwordData) => {
    await httpClient.put('/api/v1/auth/change-password', passwordData);
    // Returns nothing (204 No Content)
};