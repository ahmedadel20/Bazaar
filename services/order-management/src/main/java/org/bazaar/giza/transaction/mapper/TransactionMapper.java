package org.bazaar.giza.transaction.mapper;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.entity.Order;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.service.OrderService;
import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.entity.Transaction;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    public Transaction toTransaction(TransactionRequest request) {
        return Transaction.builder()
                .id(request.id())
                .order(Order.builder().id(request.orderId()).build())
                .paymentStatus(request.paymentStatus())
                .finalPrice(orderService.getById(request.orderId()).getFinalPrice())
                .createdAt(new Date(System.currentTimeMillis()))
                .build();
    }

    public TransactionResponse toTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .orderResponse(orderMapper.toOrderResponse(transaction.getOrder()))
                .paymentStatus(transaction.getPaymentStatus())
                .finalPrice(transaction.getFinalPrice())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
