package org.bazaar.giza.clients;

import lombok.Builder;

@Builder
public record AddressDto (
    String street,
    String city,
    String zipCode
)
{}