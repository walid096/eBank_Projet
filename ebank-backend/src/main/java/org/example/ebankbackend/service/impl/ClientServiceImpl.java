package org.example.ebankbackend.service.impl;

import org.example.ebankbackend.domain.entity.Client;
import org.example.ebankbackend.repository.ClientRepository;
import org.example.ebankbackend.service.ClientService;
import org.example.ebankbackend.web.dto.request.CreateClientRequest;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void createClient(CreateClientRequest request) {

        // RG-5: mandatory fields check
        requireClientFields(request);

        // RG-4: unique identity number
        if (clientRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new IllegalArgumentException("Numéro d'identité déjà utilisé");
        }

        // RG-6: unique email
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // Save client (UC-2)
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setIdentityNumber(request.getIdentityNumber());
        client.setBirthDate(request.getBirthDate());
        client.setEmail(request.getEmail());
        client.setPostalAddress(request.getPostalAddress());

        clientRepository.save(client);

        // TODO: RG-7 (create user + send email) in next steps
    }

    // ===== Helper methods =====

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
