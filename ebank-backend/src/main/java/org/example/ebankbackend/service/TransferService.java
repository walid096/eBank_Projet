package org.example.ebankbackend.service;

import org.example.ebankbackend.web.dto.request.TransferRequest;

public interface TransferService {
    void transfer(String login, TransferRequest request);
}
