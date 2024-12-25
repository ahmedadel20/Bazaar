package org.bazaar.giza.customer.dto;

public record CustomerRequest(Long id,
                String firstName,
                String lastName,
                String email,
                String phoneNumber) {
}
