package org.bazaar.giza.transaction.mapper;

import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.entity.Transaction;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class TransactionMapper {
    public Transaction toTransaction(TransactionRequest request) {
        return Transaction.builder()
                .id(request.id())
                .orderId(request.orderId())
                .paymentStatus(request.paymentStatus())
                .finalPrice(request.finalPrice())
                .createdAt(new Date(System.currentTimeMillis()))
                .build();
    }

    public TransactionResponse toTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .orderId(transaction.getOrderId())
                .paymentStatus(transaction.getPaymentStatus())
                .finalPrice(transaction.getFinalPrice())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
