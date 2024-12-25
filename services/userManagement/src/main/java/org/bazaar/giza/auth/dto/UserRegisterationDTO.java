package org.bazaar.giza.auth.dto;

public record UserRegisterationDTO(String firstName, String lastName, String email, String phoneNumber,
                String password) {
}
