package com.distributeur.repository;

import com.distributeur.model.Transaction;
import com.distributeur.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByStatus(TransactionStatus status);

    Optional<Transaction> findFirstByStatusOrderByCreatedAtDesc(TransactionStatus status);

    List<Transaction> findByStatusOrderByCreatedAtDesc(TransactionStatus status);

    List<Transaction> findAllByOrderByCreatedAtDesc();

    List<Transaction> findByCreatedAtAfter(LocalDateTime date);

    void deleteByStatus(TransactionStatus status);
}
