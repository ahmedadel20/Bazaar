package org.bazaar.paymentGateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.bazaar.paymentGateway.constant.ErrorMessage;
import org.bazaar.paymentGateway.payment.dto.UserAccountRequest;
import org.bazaar.paymentGateway.payment.dto.UserAccountResponse;
import org.bazaar.paymentGateway.payment.entity.UserAccount;
import org.bazaar.paymentGateway.payment.exception.PaymentGatewayException;
import org.bazaar.paymentGateway.payment.repo.UserAccountRepo;
import org.bazaar.paymentGateway.payment.service.UserAccountService;
import org.bazaar.paymentGateway.payment.service.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserAccountServiceTest {
    @TestConfiguration
    static class ServiceTestConfig {
        @Bean
        UserAccountService service(UserAccountRepo repo) {
            return new UserAccountServiceImpl(repo);
        }
    }

    @MockitoBean
    private UserAccountRepo repo;

    @Autowired
    private UserAccountService service;

    private static UserAccountRequest userAccountCreateRequest;
    private static UserAccountRequest userAccountPaymentRequest;
    private static UserAccount userAccountPreSave;
    private static UserAccount userAccountPostSave;
    private static UserAccountResponse userAccountResponse;

    @BeforeAll
    public static void setUp() {
        userAccountCreateRequest = new UserAccountRequest("Bob@gmail.com", "password", 500);
        userAccountPaymentRequest = new UserAccountRequest("Bob@gmail.com", "password", 250);

        userAccountPreSave = new UserAccount(
                null,
                userAccountCreateRequest.email(),
                userAccountCreateRequest.password(),
                userAccountCreateRequest.amountOfMoney());
        userAccountPostSave = new UserAccount(
                (long) 1,
                userAccountCreateRequest.email(),
                userAccountCreateRequest.password(),
                userAccountCreateRequest.amountOfMoney());

        userAccountResponse = new UserAccountResponse(
                userAccountPostSave.getEmail(),
                userAccountPostSave.getMoneyInAccount());
    }

    @BeforeEach
    public void setUpForEach() {
        userAccountPaymentRequest = new UserAccountRequest("Bob@gmail.com", "password", 250);
        userAccountPostSave.setMoneyInAccount(userAccountCreateRequest.amountOfMoney());

        when(repo.save(userAccountPreSave)).thenReturn(userAccountPostSave);
    }

    @Test
    public void createUserAccount_DuplicateEmail() {
        when(repo.findByEmail(userAccountCreateRequest.email())).thenReturn(Optional.of(userAccountPostSave));

        PaymentGatewayException ex = assertThrows(PaymentGatewayException.class,
                () -> {
                    service.createUserAccount(userAccountCreateRequest);
                });
        assertEquals(ErrorMessage.DUPLICATE_EMAIL, ex.getMessage());
    }

    @Test
    public void createUserAccount_Successful() {
        when(repo.findByEmail(userAccountCreateRequest.email())).thenReturn(Optional.empty());

        assertEquals(userAccountResponse, service.createUserAccount(userAccountCreateRequest));
    }

    @Test
    public void makePayment_IncorrectEmail() {
        when(repo.findByEmail(userAccountPaymentRequest.email())).thenReturn(Optional.empty());

        PaymentGatewayException ex = assertThrows(PaymentGatewayException.class,
                () -> {
                    service.makePayment(userAccountPaymentRequest);
                });
        assertEquals(ErrorMessage.INCORRECT_EMAIL_OR_PASSWORD, ex.getMessage());
    }

    @Test
    public void makePayment_IncorrectPassword() {
        when(repo.findByEmail(userAccountPaymentRequest.email())).thenReturn(Optional.of(userAccountPostSave));
        userAccountPaymentRequest = new UserAccountRequest(
                userAccountPaymentRequest.email(),
                "Wrong password",
                userAccountPaymentRequest.amountOfMoney());

        PaymentGatewayException ex = assertThrows(PaymentGatewayException.class,
                () -> {
                    service.makePayment(userAccountPaymentRequest);
                });
        assertEquals(ErrorMessage.INCORRECT_EMAIL_OR_PASSWORD, ex.getMessage());
    }

    @Test
    public void makePayment_NotEnoughMoneyInAccount() {
        when(repo.findByEmail(userAccountPaymentRequest.email())).thenReturn(Optional.of(userAccountPostSave));
        userAccountPaymentRequest = new UserAccountRequest(
                userAccountPaymentRequest.email(),
                userAccountPaymentRequest.password(),
                999.99f);

        PaymentGatewayException ex = assertThrows(PaymentGatewayException.class,
                () -> {
                    service.makePayment(userAccountPaymentRequest);
                });
        assertEquals(ErrorMessage.NOT_ENOUGH_MONEY_IN_ACCOUNT, ex.getMessage());
    }

    @Test
    public void makePayment_Successful() {
        when(repo.findByEmail(userAccountPaymentRequest.email())).thenReturn(Optional.of(userAccountPostSave));

        String result = service.makePayment(userAccountPaymentRequest);

        assertEquals("ACCEPTED: Payment Successful!", result);

        assertEquals(
                userAccountCreateRequest.amountOfMoney() - userAccountPaymentRequest.amountOfMoney(),
                userAccountPostSave.getMoneyInAccount());
    }
}
