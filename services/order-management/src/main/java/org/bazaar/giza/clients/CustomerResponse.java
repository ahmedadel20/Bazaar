package org.bazaar.giza.clients;

import lombok.Builder;

import java.util.Set;

@Builder
public record CustomerResponse(Long id, String firstName, String lastName, String email, String phoneNumber,
                               Set<AddressDto> addresses) {
}
