package org.example.ebankbackend.web.dto.response;

public class LoginResponse {

    private String token;
    private long expiresInSeconds;
    private String role;

    public LoginResponse() {}

    public LoginResponse(String token, long expiresInSeconds, String role) {
        this.token = token;
        this.expiresInSeconds = expiresInSeconds;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getExpiresInSeconds() { return expiresInSeconds; }
    public void setExpiresInSeconds(long expiresInSeconds) { this.expiresInSeconds = expiresInSeconds; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
