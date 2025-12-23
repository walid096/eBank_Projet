package org.example.ebankbackend.repository;

import org.example.ebankbackend.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByRib(String rib);

    boolean existsByRib(String rib);

    List<Account> findByClientId(Long clientId);
}
