package org.bazaar.giza.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;

@Builder
public record NotificationDto(
        @JsonProperty("recipient")
        @Email
        String recipient,
        @JsonProperty("subject")
        @NotBlank
        String subject,
        @JsonProperty("body")
        @NotBlank
        String body,
        @JsonProperty("sentAt")
        Instant sentAt
) {}
