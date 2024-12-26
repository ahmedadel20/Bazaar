package org.bazaar.paymentGateway.payment.service;

import org.bazaar.paymentGateway.payment.dto.UserAccountRequest;
import org.bazaar.paymentGateway.payment.dto.UserAccountResponse;

public interface UserAccountService {
    UserAccountResponse createUserAccount(UserAccountRequest userAccountRequest);

    String makePayment(UserAccountRequest userAccountRequest);
}
