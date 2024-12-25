package org.bazaar.giza.transaction.repository;

import org.bazaar.giza.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
