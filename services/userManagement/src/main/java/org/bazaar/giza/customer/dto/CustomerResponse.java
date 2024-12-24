package org.bazaar.giza.customer.dto;

import lombok.Builder;

@Builder
public record CustomerResponse(Long id, String firstName, String lastName, String email, String phoneNumber) {
}
