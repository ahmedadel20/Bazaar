package org.bazaar.paymentGateway.payment.controller;

import org.bazaar.paymentGateway.payment.dto.UserAccountRequest;
import org.bazaar.paymentGateway.payment.dto.UserAccountResponse;
import org.bazaar.paymentGateway.payment.service.UserAccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "PaymentGateway", description = "Controller for handling mappings for payment gateway.")
@AllArgsConstructor
@RestController
@RequestMapping("/paybuddy")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @PostMapping()
    public UserAccountResponse createUserAccount(@Valid @RequestBody UserAccountRequest userAccountRequest) {
        return userAccountService.createUserAccount(userAccountRequest);
    }

    @PutMapping()
    public String makePayment(@Valid @RequestBody UserAccountRequest userAccountRequest) {
        return userAccountService.makePayment(userAccountRequest);
    }

}
