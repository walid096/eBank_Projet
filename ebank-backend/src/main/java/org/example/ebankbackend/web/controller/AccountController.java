package org.example.ebankbackend.web.controller;

import jakarta.validation.Valid;
import org.example.ebankbackend.service.AccountService;
import org.example.ebankbackend.web.dto.request.CreateAccountRequest;
import org.example.ebankbackend.web.dto.response.AccountDashboardResponse;
import org.example.ebankbackend.web.dto.response.AccountSummaryResponse;
import org.example.ebankbackend.web.dto.response.OperationPageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // UC-3 — Create bank account (AGENT_GUICHET)
    @PostMapping
    @PreAuthorize("hasRole('AGENT_GUICHET')")
    public ResponseEntity<Void> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        accountService.createAccount(request);
        return ResponseEntity.status(201).build();
    }

    // UC-4 — List my accounts (CLIENT)
    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<AccountSummaryResponse>> getMyAccounts(Authentication authentication) {
        return ResponseEntity.ok(accountService.getMyAccounts(authentication.getName()));
    }

    // UC-4 — Dashboard for one account (CLIENT): rib + balance + last 10
    @GetMapping("/me/dashboard")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AccountDashboardResponse> getMyDashboard(
            @RequestParam(required = false) String rib,
            Authentication authentication
    ) {
        return ResponseEntity.ok(accountService.getMyDashboard(authentication.getName(), rib));
    }


    // UC-4 — Pagination for other operations (CLIENT)
    @GetMapping("/me/operations")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OperationPageResponse> getMyOperations(
            @RequestParam String rib,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                accountService.getMyOperations(authentication.getName(), rib, page, size)
        );
    }
}
