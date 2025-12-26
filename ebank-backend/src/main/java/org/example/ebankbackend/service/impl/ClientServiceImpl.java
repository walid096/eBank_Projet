package org.example.ebankbackend.service.impl;

import org.example.ebankbackend.domain.entity.Client;
import org.example.ebankbackend.domain.entity.User;
import org.example.ebankbackend.domain.enums.Role;
import org.example.ebankbackend.repository.ClientRepository;
import org.example.ebankbackend.repository.UserRepository;
import org.example.ebankbackend.service.ClientService;
import org.example.ebankbackend.service.EmailService;
import org.example.ebankbackend.util.PasswordGenerator;
import org.example.ebankbackend.web.dto.request.CreateClientRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ClientServiceImpl(
            ClientRepository clientRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void createClient(CreateClientRequest request) {

        // RG-5
        requireClientFields(request);

        String identityNumber = request.getIdentityNumber().trim();
        String email = request.getEmail().trim();
        String firstName = request.getFirstName().trim();
        String lastName = request.getLastName().trim();
        String postalAddress = request.getPostalAddress().trim();

        // RG-4
        if (clientRepository.existsByIdentityNumber(identityNumber)) {
            throw new IllegalArgumentException("Numéro d'identité déjà utilisé");
        }

        // RG-6
        if (clientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // UC-2: save client
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setIdentityNumber(identityNumber);
        client.setBirthDate(request.getBirthDate());
        client.setEmail(email);
        client.setPostalAddress(postalAddress);

        client = clientRepository.save(client);

        // RG-7: create user
        String login = identityNumber;

        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Login déjà utilisé");
        }

        String rawPassword = PasswordGenerator.generate();
        String passwordHash = passwordEncoder.encode(rawPassword); // RG-1

        User user = new User();
        user.setLogin(login);
        user.setPasswordHash(passwordHash);
        user.setRole(Role.CLIENT);
        user.setClient(client);

        userRepository.save(user);

        // RG-7: send email with credentials
        emailService.sendCredentials(email, login, rawPassword);
    }

    // ===== Helpers =====

    private void requireClientFields(CreateClientRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Champs client obligatoires manquants");
        }
        if (isBlank(request.getFirstName())
                || isBlank(request.getLastName())
                || isBlank(request.getIdentityNumber())
                || request.getBirthDate() == null
                || isBlank(request.getEmail())
                || isBlank(request.getPostalAddress())) {
            throw new IllegalArgumentException("Champs client obligatoires manquants");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}