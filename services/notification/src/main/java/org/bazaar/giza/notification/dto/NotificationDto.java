package org.bazaar.giza.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;

@Builder
public record NotificationDto(
        @JsonProperty("recipient")
        @NotBlank
        String recipient,
        @JsonProperty("subject")
        @NotBlank
        String subject,
        @JsonProperty("body")
        @NotBlank
        String body,
        @JsonProperty("sentAt") Instant sentAt
)
{}