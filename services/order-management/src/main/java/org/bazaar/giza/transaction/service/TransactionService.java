package org.bazaar.giza.transaction.service;

import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {
    public TransactionResponse create(TransactionRequest transactionRequest);

    public TransactionResponse update(TransactionRequest request);

    String delete(Long transactionId);

    public TransactionResponse getById(Long transactionId);

    public List<TransactionResponse> getAll();
}
