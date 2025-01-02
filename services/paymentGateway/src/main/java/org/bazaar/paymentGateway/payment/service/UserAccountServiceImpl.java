package org.bazaar.paymentGateway.payment.service;

import java.util.Optional;

import org.bazaar.paymentGateway.constant.ErrorMessage;
import org.bazaar.paymentGateway.payment.dto.UserAccountRequest;
import org.bazaar.paymentGateway.payment.dto.UserAccountResponse;
import org.bazaar.paymentGateway.payment.entity.UserAccount;
import org.bazaar.paymentGateway.payment.exception.PaymentGatewayException;
import org.bazaar.paymentGateway.payment.repo.UserAccountRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepo repository;

    @Override
    public UserAccountResponse createUserAccount(UserAccountRequest userAccountRequest) {
        boolean emailExists = repository.findByEmail(userAccountRequest.email()).isPresent();
        if (emailExists) {
            throw new PaymentGatewayException(ErrorMessage.DUPLICATE_EMAIL);
        }
        UserAccount userAccount = UserAccount.builder()
                .email(userAccountRequest.email())
                .password(userAccountRequest.password())
                .moneyInAccount(userAccountRequest.amountOfMoney())
                .build();
        userAccount = repository.save(userAccount);
        return new UserAccountResponse(userAccount.getEmail(), userAccount.getMoneyInAccount());
    }

    @Override
    public String makePayment(UserAccountRequest userAccountRequest) {
        Optional<UserAccount> userAccountOptional = repository.findByEmail(userAccountRequest.email());
        if (userAccountOptional.isEmpty()) {
            throw new PaymentGatewayException(ErrorMessage.INCORRECT_EMAIL_OR_PASSWORD);
        }
        UserAccount userAccount = userAccountOptional.get();
        if (!userAccount.getPassword().equals(userAccountRequest.password())) {
            throw new PaymentGatewayException(ErrorMessage.INCORRECT_EMAIL_OR_PASSWORD);
        }

        if (userAccount.getMoneyInAccount().compareTo(userAccountRequest.amountOfMoney()) == -1) {
            throw new PaymentGatewayException(ErrorMessage.NOT_ENOUGH_MONEY_IN_ACCOUNT);
        }

        userAccount.setMoneyInAccount(userAccount.getMoneyInAccount().subtract(userAccountRequest.amountOfMoney()));
        repository.save(userAccount);
        return "ACCEPTED: Payment Successful!";
    }
}
