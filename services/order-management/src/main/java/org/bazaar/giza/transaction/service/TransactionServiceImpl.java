package org.bazaar.giza.transaction.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.service.OrderService;
import org.bazaar.giza.transaction.dto.NotificationDto;
import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.exception.TransactionNotFoundException;
import org.bazaar.giza.transaction.mapper.TransactionMapper;
import org.bazaar.giza.transaction.repository.TransactionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    @Value("${rabbitmq.exchange.notification}")
    private String notificationExchange;
    @Value("${rabbitmq.routing.transaction}")
    private String transactionRoutingKey;
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final TransactionMapper transactionMapper;
    private final OrderMapper orderMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public TransactionResponse create(TransactionRequest request) {
        var transaction = transactionMapper.toTransaction(request);
        transaction.setId(null);
        var orderResponse = orderService.getById(request.orderId());
        transaction.setOrder(orderMapper.toOrder(orderResponse));
        transaction.setFinalPrice(orderResponse.finalPrice());
        var savedTransaction = transactionRepository.save(transaction);

        NotificationDto notificationDto = NotificationDto.builder()

                //TODO: get email from jwt token
                .recipient("abdalla.maged95@gmail.com") // Replace with actual user email

                .subject("Transaction Confirmation")
                .body("Your transaction for order #" + request.orderId() + " is confirmed.")
                .sentAt(Instant.now())
                .build();

        rabbitTemplate.convertAndSend(notificationExchange, transactionRoutingKey, notificationDto);

        return transactionMapper.toTransactionResponse(savedTransaction);
    }

    @Override
    public TransactionResponse update(TransactionRequest request) {
        // Check if transaction exists
        var existingTransaction = transactionRepository.findById(request.id())
                .orElseThrow(() -> new TransactionNotFoundException(request.id()));

        // Update only allowed fields
        existingTransaction.setPaymentStatus(request.paymentStatus());

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
