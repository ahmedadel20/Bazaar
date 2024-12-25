package org.bazaar.paymentGateway.payment.service;

import java.util.Optional;

import org.bazaar.paymentGateway.payment.dto.UserAccountRequest;
import org.bazaar.paymentGateway.payment.dto.UserAccountResponse;
import org.bazaar.paymentGateway.payment.entity.UserAccount;
import org.bazaar.paymentGateway.payment.repo.UserAccountRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepo repository;

    @Override
    public UserAccountResponse createUserAccount(UserAccountRequest userAccountRequest) {
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
            return "DECLINED: Incorrect username or password.";
        }
        UserAccount userAccount = userAccountOptional.get();
        if (userAccount.getPassword() != userAccountRequest.password()) {
            return "DECLINED: Incorrect username or password.";
        }

        if (userAccount.getMoneyInAccount() < userAccountRequest.amountOfMoney()) {
            return "DECLINED: Not enough money in account.";
        }

        userAccount.setMoneyInAccount(userAccount.getMoneyInAccount() - userAccountRequest.amountOfMoney());
        repository.save(userAccount);
        return "ACCEPTED: Payment Successful!";
    }
}
