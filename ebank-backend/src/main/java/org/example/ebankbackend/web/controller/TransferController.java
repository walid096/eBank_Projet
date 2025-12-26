package org.example.ebankbackend.web.controller;

import jakarta.validation.Valid;
import org.example.ebankbackend.service.TransferService;
import org.example.ebankbackend.web.dto.request.TransferRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> createTransfer(
            @Valid @RequestBody TransferRequest request,
            Authentication authentication
    ) {
        transferService.transfer(authentication.getName(), request);
        return ResponseEntity.status(201).build();
    }
}
