package org.bazaar.giza.clients;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserAccountRequest(
    String email,
    String password,
    BigDecimal amountOfMoney
){}