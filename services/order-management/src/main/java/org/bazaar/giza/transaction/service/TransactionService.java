package org.bazaar.giza.transaction.service;

import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction create(Transaction transaction);

    Transaction update(Transaction request);

    String delete(Long transactionId);

    Transaction getById(Long transactionId);

    List<Transaction> getAll();
}
