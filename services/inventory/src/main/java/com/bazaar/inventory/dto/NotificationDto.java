package com.bazaar.inventory.dto;

import lombok.Builder;
import java.time.Instant;

@Builder
public record NotificationDto(
        String recipient,
        String subject,
        String body,
        Instant sentAt
)
{
}
