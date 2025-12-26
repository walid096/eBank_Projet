package org.example.ebankbackend.service.impl;

import org.example.ebankbackend.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendCredentials(String toEmail, String login, String rawPassword) {

        System.out.println("[RG-7 EMAIL] to=" + toEmail + " | login=" + login + " | password=" + rawPassword);
    }
}
