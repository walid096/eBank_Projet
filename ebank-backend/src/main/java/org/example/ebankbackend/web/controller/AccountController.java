package org.example.ebankbackend.web.controller;

import jakarta.validation.Valid;
import org.example.ebankbackend.service.AccountService;
import org.example.ebankbackend.web.dto.request.CreateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @PreAuthorize("hasRole('AGENT_GUICHET')")
    public ResponseEntity<Void> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        accountService.createAccount(request);
        return ResponseEntity.status(201).build();
    }
}
