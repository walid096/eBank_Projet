package org.example.ebankbackend.service.impl;

import org.example.ebankbackend.domain.entity.User;
import org.example.ebankbackend.repository.UserRepository;
import org.example.ebankbackend.security.jwt.JwtService;
import org.example.ebankbackend.service.AuthService;
import org.example.ebankbackend.web.dto.request.LoginRequest;
import org.example.ebankbackend.web.dto.response.LoginResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String BAD_CREDENTIALS_MESSAGE = "Login ou mot de passe erronés";
    private static final long EXPIRES_IN_SECONDS = 3600;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new BadCredentialsException(BAD_CREDENTIALS_MESSAGE));

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!matches) {
            throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, EXPIRES_IN_SECONDS, user.getRole().name());
    }
}
