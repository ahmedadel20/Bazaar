package org.bazaar.giza.customer.dto;

import java.util.Set;

import org.bazaar.giza.customer.entity.Address;

import lombok.Builder;

@Builder
public record CustomerResponse(Long id, String firstName, String lastName, String email, String phoneNumber,
        Set<Address> addresses) {
}
