package org.example.ebankbackend.service.impl;

import org.example.ebankbackend.domain.entity.Account;
import org.example.ebankbackend.domain.entity.Operation;
import org.example.ebankbackend.domain.entity.User;
import org.example.ebankbackend.domain.enums.AccountStatus;
import org.example.ebankbackend.domain.enums.OperationType;
import org.example.ebankbackend.repository.AccountRepository;
import org.example.ebankbackend.repository.OperationRepository;
import org.example.ebankbackend.repository.UserRepository;
import org.example.ebankbackend.service.TransferService;
import org.example.ebankbackend.web.dto.request.TransferRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferServiceImpl implements TransferService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public TransferServiceImpl(
            UserRepository userRepository,
            AccountRepository accountRepository,
            OperationRepository operationRepository
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    @Transactional
    public void transfer(String login, TransferRequest request) {

        // ===== basic checks =====
        if (isBlank(login)) {
            throw new IllegalArgumentException("Login manquant");
        }
        if (request == null
                || isBlank(request.getSourceRib())
                || isBlank(request.getDestinationRib())
                || request.getAmount() == null
                || isBlank(request.getMotif())) {
            throw new IllegalArgumentException("Champs virement obligatoires manquants");
        }

        String sourceRib = request.getSourceRib().trim();
        String destinationRib = request.getDestinationRib().trim();
        BigDecimal amount = request.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }

        // ===== load user =====
        User user = userRepository.findByLogin(login.trim())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        if (user.getClient() == null) {
            throw new IllegalArgumentException("Aucun client associé à cet utilisateur");
        }

        // ===== load accounts =====
        Account source = accountRepository.findByRib(sourceRib)
                .orElseThrow(() -> new IllegalArgumentException("Compte source introuvable"));

        Account destination = accountRepository.findByRib(destinationRib)
                .orElseThrow(() -> new IllegalArgumentException("Compte destination introuvable"));

        // ===== must belong to logged-in client =====
        if (!source.getClient().getId().equals(user.getClient().getId())) {
            throw new IllegalArgumentException(
                    "Vous n'avez pas le droit d'accéder à cette fonctionnalité. Veuillez contacter votre administrateur"
            );
        }

        // ===== RG_11: account not blocked/closed =====
        if (source.getStatus() == AccountStatus.BLOQUE || source.getStatus() == AccountStatus.CLOTURE) {
            throw new IllegalArgumentException("Compte source bloqué ou fermé");
        }
        if (destination.getStatus() == AccountStatus.BLOQUE || destination.getStatus() == AccountStatus.CLOTURE) {
            throw new IllegalArgumentException("Compte destination bloqué ou fermé");
        }

        // ===== RG_12: balance > amount =====
        if (source.getBalance() == null || source.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Solde insuffisant");
        }

        // ===== RG_13: debit source =====
        source.setBalance(source.getBalance().subtract(amount));

        // ===== RG_14: credit destination =====
        if (destination.getBalance() == null) {
            destination.setBalance(BigDecimal.ZERO);
        }
        destination.setBalance(destination.getBalance().add(amount));

        // save balances
        accountRepository.save(source);
        accountRepository.save(destination);

        // ===== RG_15: trace both operations with exact same datetime =====
        LocalDateTime now = LocalDateTime.now();

        // UC-4: Format operation labels according to requirement
        // Debit: "Virement vers [destination RIB] - [motif]"
        // Credit: "Virement en votre faveur de [source RIB] - [motif]"
        String debitLabel = String.format("Virement vers %s - %s", destinationRib, request.getMotif().trim());
        String creditLabel = String.format("Virement en votre faveur de %s - %s", sourceRib, request.getMotif().trim());

        Operation debitOp = new Operation();
        debitOp.setAccount(source);
        debitOp.setType(OperationType.DEBIT);
        debitOp.setAmount(amount);
        debitOp.setOperationDateTime(now);
        debitOp.setLabel(debitLabel);

        Operation creditOp = new Operation();
        creditOp.setAccount(destination);
        creditOp.setType(OperationType.CREDIT);
        creditOp.setAmount(amount);
        creditOp.setOperationDateTime(now);
        creditOp.setLabel(creditLabel);

        operationRepository.save(debitOp);
        operationRepository.save(creditOp);
    }

    private boolean isBlank(String v) {
        return v == null || v.trim().isEmpty();
    }
}