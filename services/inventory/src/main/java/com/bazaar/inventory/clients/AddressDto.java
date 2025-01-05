package com.bazaar.inventory.clients;

import lombok.Builder;

@Builder
public record AddressDto (
    String street,
    String city,
    String zipCode
)
{}