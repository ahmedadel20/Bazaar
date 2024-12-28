package org.bazaar.giza.notification.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDto(
        String recipient,
        String subject,
        String body,
        LocalDateTime sentAt
) {}