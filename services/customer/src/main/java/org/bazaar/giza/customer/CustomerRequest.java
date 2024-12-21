package org.bazaar.giza.customer;

public record CustomerRequest(Long id,
                              String firstName,
                              String lastName,
                              String email) {
}
