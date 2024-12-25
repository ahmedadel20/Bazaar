package org.bazaar.paymentGateway.payment.service;

import org.bazaar.paymentGateway.payment.repo.UserAccountRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepo repository;

    @Override
    public boolean makePayment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makePayment'");
    }

}
