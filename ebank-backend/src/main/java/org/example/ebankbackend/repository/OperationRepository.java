package org.example.ebankbackend.repository;

import org.example.ebankbackend.domain.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    Page<Operation> findByAccountIdOrderByOperationDateTimeDesc(Long accountId, Pageable pageable);
}
