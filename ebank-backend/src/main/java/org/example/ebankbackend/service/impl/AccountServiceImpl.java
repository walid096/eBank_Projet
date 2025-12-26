package org.example.ebankbackend.service.impl;

import org.example.ebankbackend.domain.entity.Account;
import org.example.ebankbackend.domain.entity.Client;
import org.example.ebankbackend.domain.entity.Operation;
import org.example.ebankbackend.domain.entity.User;
import org.example.ebankbackend.domain.enums.AccountStatus;
import org.example.ebankbackend.repository.AccountRepository;
import org.example.ebankbackend.repository.ClientRepository;
import org.example.ebankbackend.repository.OperationRepository;
import org.example.ebankbackend.repository.UserRepository;
import org.example.ebankbackend.service.AccountService;
import org.example.ebankbackend.web.dto.request.CreateAccountRequest;
import org.example.ebankbackend.web.dto.response.AccountDashboardResponse;
import org.example.ebankbackend.web.dto.response.AccountSummaryResponse;
import org.example.ebankbackend.web.dto.response.OperationLineResponse;
import org.example.ebankbackend.web.dto.response.OperationPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final OperationRepository operationRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            ClientRepository clientRepository,
            UserRepository userRepository,
            OperationRepository operationRepository
    ) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
    }


    @Override
    public void createAccount(CreateAccountRequest request) {

        if (request == null
                || isBlank(request.getRib())
                || isBlank(request.getIdentityNumber())) {
            throw new IllegalArgumentException("Champs compte obligatoires manquants");
        }

        String rib = request.getRib().trim();
        String identityNumber = request.getIdentityNumber().trim();

        if (!isValidRib(rib)) {
            throw new IllegalArgumentException("RIB invalide");
        }

        if (accountRepository.existsByRib(rib)) {
            throw new IllegalArgumentException("RIB déjà utilisé");
        }

        Client client = clientRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        Account account = new Account();
        account.setClient(client);
        account.setRib(rib);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.OUVERT);

        accountRepository.save(account);
    }


    @Override
    public List<AccountSummaryResponse> getMyAccounts(String login) {
        User user = getUserByLogin(login);

        if (user.getClient() == null) {
            throw new IllegalArgumentException("Aucun client associé à cet utilisateur");
        }

        Long clientId = user.getClient().getId();

        return accountRepository.findByClientId(clientId).stream()
                .map(a -> new AccountSummaryResponse(
                        a.getRib(),
                        a.getBalance(),
                        a.getStatus() == null ? null : a.getStatus().name()
                ))
                .toList();
    }

    // =========================
    // dashboard for one account (rib)
    // =========================
    @Override
    public AccountDashboardResponse getMyDashboard(String login, String rib) {

        User user = getUserByLogin(login);

        if (user.getClient() == null) {
            throw new IllegalArgumentException("Aucun client associé à cet utilisateur");
        }

        Account account;

        //  If rib provided -> normal behavior
        if (!isBlank(rib)) {
            account = accountRepository.findByRib(rib.trim())
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));

            assertAccountBelongsToUserClient(account, user);

        } else {
            //  rib missing -> choose default account = most recently moved
            Long clientId = user.getClient().getId();
            List<Account> accounts = accountRepository.findByClientId(clientId);

            if (accounts.isEmpty()) {
                throw new IllegalArgumentException("Compte introuvable");
            }

            Account best = null;
            java.time.LocalDateTime bestDate = null;

            for (Account a : accounts) {
                List<Operation> ops = operationRepository
                        .findByAccountIdOrderByOperationDateTimeDesc(a.getId(), PageRequest.of(0, 1))
                        .getContent();

                if (ops.isEmpty()) continue;

                java.time.LocalDateTime d = ops.get(0).getOperationDateTime();
                if (bestDate == null || (d != null && d.isAfter(bestDate))) {
                    bestDate = d;
                    best = a;
                }
            }

            // if no operations at all -> take first account
            account = (best != null) ? best : accounts.get(0);
        }

        List<Operation> last10 = operationRepository
                .findByAccountIdOrderByOperationDateTimeDesc(account.getId(), PageRequest.of(0, 10))
                .getContent();

        List<OperationLineResponse> ops = last10.stream()
                .map(op -> new OperationLineResponse(
                        op.getLabel(),
                        op.getType() == null ? null : op.getType().name(),
                        op.getOperationDateTime(),
                        op.getAmount()
                ))
                .toList();

        return new AccountDashboardResponse(
                account.getRib(),
                account.getBalance(),
                ops
        );

    }



    // (CLIENT) - pagination for other operations

    @Override
    public OperationPageResponse getMyOperations(String login, String rib, int page, int size) {

        User user = getUserByLogin(login);

        if (isBlank(rib)) {
            throw new IllegalArgumentException("RIB manquant");
        }

        // Safety limits (avoid abuse)
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 50) size = 50;

        Account account = accountRepository.findByRib(rib.trim())
                .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));

        assertAccountBelongsToUserClient(account, user);

        Page<Operation> p = operationRepository.findByAccountIdOrderByOperationDateTimeDesc(
                account.getId(),
                PageRequest.of(page, size)
        );

        List<OperationLineResponse> items = p.getContent().stream()
                .map(op -> new OperationLineResponse(
                        op.getLabel(),
                        op.getType() == null ? null : op.getType().name(),
                        op.getOperationDateTime(),
                        op.getAmount()
                ))
                .toList();

        return new OperationPageResponse(
                items,
                p.getNumber(),
                p.getSize(),
                p.getTotalElements(),
                p.getTotalPages(),
                p.isLast()
        );
    }

    //HELPERS :
    private User getUserByLogin(String login) {
        if (isBlank(login)) {
            throw new IllegalArgumentException("Login manquant");
        }

        return userRepository.findByLogin(login.trim())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
    }

    private void assertAccountBelongsToUserClient(Account account, User user) {
        if (user.getClient() == null) {
            throw new IllegalArgumentException("Aucun client associé à cet utilisateur");
        }
        if (!account.getClient().getId().equals(user.getClient().getId())) {
            throw new IllegalArgumentException(
                    "Vous n’avez pas le droit d’accéder à cette fonctionnalité. Veuillez contacter votre administrateur"
            );
        }
    }

    private boolean isValidRib(String rib) {
        return rib != null && rib.matches("\\d{24}");
    }

    private boolean isBlank(String v) {
        return v == null || v.trim().isEmpty();
    }
}
