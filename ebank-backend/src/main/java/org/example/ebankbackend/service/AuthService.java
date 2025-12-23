package org.example.ebankbackend.service;

import org.example.ebankbackend.web.dto.request.ChangePasswordRequest;
import org.example.ebankbackend.web.dto.request.LoginRequest;
import org.example.ebankbackend.web.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void changePassword(ChangePasswordRequest request);
}
