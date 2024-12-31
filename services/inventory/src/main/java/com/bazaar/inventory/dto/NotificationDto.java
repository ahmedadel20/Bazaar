package com.bazaar.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record NotificationDto(
        @JsonProperty("recipient") String recipient,
        @JsonProperty("subject") String subject,
        @JsonProperty("body") String body,
        @JsonProperty("sentAt") Instant sentAt
) {}