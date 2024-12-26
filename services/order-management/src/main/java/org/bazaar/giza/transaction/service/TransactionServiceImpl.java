package org.bazaar.giza.transaction.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.service.OrderService;
import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.exception.TransactionNotFoundException;
import org.bazaar.giza.transaction.mapper.TransactionMapper;
import org.bazaar.giza.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final TransactionMapper transactionMapper;
    private final OrderMapper orderMapper;

    @Override
    public TransactionResponse create(TransactionRequest request) {
        var transaction = transactionMapper.toTransaction(request);
        transaction.setId(null);
        var orderResponse = orderService.getById(request.orderId());
        transaction.setOrder(orderMapper.toOrder(orderResponse));
        return transactionMapper.toTransactionResponse(transactionRepository.save(transaction));
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

    @Override
    public TransactionResponse getById(Long transactionId) {
        return transactionMapper.toTransactionResponse(transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotFoundException(transactionId)));
    }

    @Override
    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll().stream().map(transactionMapper::toTransactionResponse).toList();
    }
}
