package org.example.ebankbackend.service;

import org.example.ebankbackend.web.dto.request.CreateClientRequest;

public interface ClientService {

    void createClient(CreateClientRequest request);
}
