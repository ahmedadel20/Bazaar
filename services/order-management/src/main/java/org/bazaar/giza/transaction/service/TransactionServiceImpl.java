package org.bazaar.giza.transaction.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.service.OrderService;
import org.bazaar.giza.transaction.dto.NotificationDto;
import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.entity.Transaction;
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
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Transaction create(Transaction transaction) {
        transaction.setId(null);
        var order = transaction.getOrder();
        transaction.setOrder(order);
        transaction.setFinalPrice(order.getFinalPrice());
        var savedTransaction = transactionRepository.save(transaction);

        NotificationDto notificationDto = NotificationDto.builder()

                //TODO: get email from jwt token
                .recipient("abdalla.maged95@gmail.com") // Replace with actual user email

                .subject("Transaction Confirmation")
                .body("Your transaction for order #" + order.getId() + " is confirmed.")
                .sentAt(Instant.now())
                .build();

        rabbitTemplate.convertAndSend(notificationExchange, transactionRoutingKey, notificationDto);

        return savedTransaction;
    }

    @Override
    public Transaction update(Transaction transaction) {
        // Check if transaction exists
        var existingTransaction = transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new TransactionNotFoundException(transaction.getId()));

        // Update only allowed fields
        existingTransaction.setPaymentStatus(transaction.getPaymentStatus());

        // Save updated transaction
        return transactionRepository.save(existingTransaction);
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
    public Transaction getById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }
}
