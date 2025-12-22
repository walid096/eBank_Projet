package org.example.ebankbackend.web.controller;

import jakarta.validation.Valid;
import org.example.ebankbackend.service.ClientService;
import org.example.ebankbackend.web.dto.request.CreateClientRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @PreAuthorize("hasRole('AGENT_GUICHET')")
    public ResponseEntity<Void> createClient(@Valid @RequestBody CreateClientRequest request) {
        clientService.createClient(request);
        return ResponseEntity.status(201).build();
    }
}
