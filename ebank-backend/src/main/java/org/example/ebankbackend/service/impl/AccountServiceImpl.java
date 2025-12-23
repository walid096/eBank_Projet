package org.example.ebankbackend.service.impl;

import org.example.ebankbackend.domain.entity.Account;
import org.example.ebankbackend.domain.entity.Client;
import org.example.ebankbackend.domain.enums.AccountStatus;
import org.example.ebankbackend.repository.AccountRepository;
import org.example.ebankbackend.repository.ClientRepository;
import org.example.ebankbackend.service.AccountService;
import org.example.ebankbackend.web.dto.request.CreateAccountRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void createAccount(CreateAccountRequest request) {

        // Required by UC-3: RIB + identité client (RG_8 / RG_9)
        if (request == null
                || request.getRib() == null || request.getRib().trim().isEmpty()
                || request.getIdentityNumber() == null || request.getIdentityNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Champs compte obligatoires manquants");
        }

        String rib = request.getRib().trim();
        String identityNumber = request.getIdentityNumber().trim();

        // RG_9: RIB must be valid (PDF does not define format -> keep minimal rule)
        if (!isValidRib(rib)) {
            throw new IllegalArgumentException("RIB invalide");
        }

        // Prevent duplicates (clean + avoids DB unique constraint crash)
        if (accountRepository.findByRib(rib).isPresent()) {
            throw new IllegalArgumentException("RIB déjà utilisé");
        }

        // RG_8: identity number must exist in DB
        Client client = clientRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        Account account = new Account();
        account.setClient(client);
        account.setRib(rib);

        // PDF does NOT mention initial balance -> keep simple
        account.setBalance(BigDecimal.ZERO);

        // RG_10: status must be "Ouvert"
        account.setStatus(AccountStatus.OUVERT);

        accountRepository.save(account);
    }

    private boolean isValidRib(String rib) {
        // Minimal validation: digits only + length 24 (adjust if your course defines another format)
        return rib.matches("\\d{24}");
    }
}
