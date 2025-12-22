package org.example.ebankbackend.repository;

import org.example.ebankbackend.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByIdentityNumber(String identityNumber);

    boolean existsByIdentityNumber(String identityNumber);

    boolean existsByEmail(String email);
}
