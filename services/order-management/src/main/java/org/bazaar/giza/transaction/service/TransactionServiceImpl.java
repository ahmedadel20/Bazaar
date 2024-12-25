package org.bazaar.giza.transaction.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.transaction.exception.TransactionNotFoundException;
import org.bazaar.giza.transaction.repository.TransactionRepository;
import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.mapper.TransactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponse create(TransactionRequest request) {
        return transactionMapper.toTransactionResponse(transactionRepository.save(transactionMapper.toTransaction(request)));
    }

    @Override
    public TransactionResponse update(TransactionRequest request) {
        // Check if transaction exists
        var existingTransaction = transactionRepository.findById(request.id())
                .orElseThrow(() -> new TransactionNotFoundException(request.id()));

        // Update only allowed fields
        existingTransaction.setPaymentStatus(request.paymentStatus());
        existingTransaction.setFinalPrice(request.finalPrice());

        // Save updated transaction
        return transactionMapper.toTransactionResponse(transactionRepository.save(existingTransaction));
    }

    @Override
    public String delete(Long transactionId) {
        // Check if transaction exists
        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException(transactionId);
        }
        transactionRepository.deleteById(transactionId);
        return "Transaction removed successfully";
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getById(Long transactionId) {
        return transactionMapper.toTransactionResponse(transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotFoundException(transactionId)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll().stream().map(transactionMapper::toTransactionResponse).toList();
    }
}
