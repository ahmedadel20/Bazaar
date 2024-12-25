package org.bazaar.giza.transaction.service;

import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest transactionRequest);

    TransactionResponse update(TransactionRequest request);

    String delete(Long transactionId);

    TransactionResponse getById(Long transactionId);

    List<TransactionResponse> getAll();
}
