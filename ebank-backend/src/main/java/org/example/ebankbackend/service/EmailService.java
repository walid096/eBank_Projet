package org.example.ebankbackend.service;

public interface EmailService {
    void sendCredentials(String toEmail, String login, String rawPassword);
}
