package org.bazaar.giza.services;

import org.bazaar.giza.clients.CustomerResponse;
import org.bazaar.giza.clients.PaymentGatewayClient;
import org.bazaar.giza.clients.UserManagementClient;
import org.bazaar.giza.order.entity.Order;
import org.bazaar.giza.transaction.entity.Transaction;
import org.bazaar.giza.transaction.exception.TransactionNotFoundException;
import org.bazaar.giza.transaction.repository.TransactionRepository;
import org.bazaar.giza.transaction.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepo;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private UserManagementClient userManagementClient;
    @Mock
    private PaymentGatewayClient paymentGatewayClient;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    private static Transaction existingTransaction;
    private static Transaction newTransaction;
    private static Order order;

    @BeforeEach
    void setup() {
        order = Order.builder()
                .id(1L)
                .bazaarUserId(1L)
                .description("Test Existing Order")
                .finalPrice(BigDecimal.TEN)
                .orderDate(new Date(System.currentTimeMillis()))
                .build();
        existingTransaction = Transaction.builder()
                .id(1L)
                .order(order)
                .createdAt(new Date(System.currentTimeMillis()))
                .paymentStatus("Completed")
                .finalPrice(BigDecimal.TEN)
                .build();
        newTransaction = Transaction.builder()
                .id(2L)
                .order(order)
                .createdAt(new Date(System.currentTimeMillis()))
                .paymentStatus("Completed")
                .finalPrice(BigDecimal.TEN)
                .build();
    }

    @Test
    void testCreatingNewTransaction() {
        Mockito.when(transactionRepo.save(newTransaction))
                .then(i -> {
                    newTransaction.setId(1L);
                    return newTransaction;
                });
        Mockito.when(userManagementClient.getSingleCustomer(Mockito.any(Long.class)))
                .thenReturn(CustomerResponse.builder().build());
        assert(
                compareTransaction(
                        existingTransaction,
                        transactionService.create(newTransaction)
                )
        );
    }

    @Test
    void testUpdatingTransactionStatus() {
        Mockito.when(transactionRepo.findById(existingTransaction.getId()))
                .thenReturn(Optional.of(existingTransaction));
        Mockito.when(transactionRepo.save(existingTransaction))
                .thenReturn(existingTransaction);
        existingTransaction.setPaymentStatus("Test Status");
        assertEquals(
                "Test Status",
                transactionService.update(existingTransaction).getPaymentStatus()
        );
    }

    @Test
    void testUpdatingNonExistingTransaction() {
        Mockito.when(transactionRepo.findById(existingTransaction.getId()))
                .thenReturn(Optional.empty());
        Mockito.when(transactionRepo.save(existingTransaction))
                .thenReturn(existingTransaction);
        existingTransaction.setPaymentStatus("Test Status");
        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.update(existingTransaction)
        );
    }

    @Test
    void testDeletingTransaction() {
        Mockito.when(transactionRepo.existsById(existingTransaction.getId()))
                .thenReturn(true);
        assertEquals(
                "Transaction removed successfully",
                transactionService.delete(existingTransaction.getId())
        );
    }

    @Test
    void testDeletingNonExistingTransaction() {
        Mockito.when(transactionRepo.existsById(existingTransaction.getId()))
                .thenReturn(false);
        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.delete(existingTransaction.getId())
        );
    }

    @Test
    void testFindingTransaction() {
        Mockito.when(transactionRepo.findById(existingTransaction.getId()))
                .thenReturn(Optional.of(existingTransaction));
        assert(
                compareTransaction(
                        existingTransaction,
                        transactionService.getById(existingTransaction.getId())
                )
        );
    }

    @Test
    void testFindingNonExistingTransaction() {
        Mockito.when(transactionRepo.findById(existingTransaction.getId()))
                .thenReturn(Optional.empty());
        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.getById(existingTransaction.getId())
        );
    }

    private boolean compareTransaction(Transaction trans1, Transaction trans2) {
        return
                (trans1.getId().compareTo(trans2.getId()) == 0) &&
                        (trans1.getOrder().getId().compareTo(trans2.getOrder().getId()) == 0) &&
                        (trans1.getPaymentStatus().equals(trans2.getPaymentStatus())) &&
                        (trans1.getFinalPrice().compareTo(trans2.getFinalPrice()) == 0);
    }
}
