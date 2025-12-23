package org.example.ebankbackend.service;

import org.example.ebankbackend.web.dto.request.CreateAccountRequest;

public interface AccountService {

    void createAccount(CreateAccountRequest request);
}
