package org.bazaar.giza.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;

import org.bazaar.giza.constant.ValidationMessage;

@Builder
public record NotificationDto(
                @JsonProperty("recipient") @NotBlank(message = "recipient"
                                + ValidationMessage.NOT_BLANK) String recipient,
                @JsonProperty("subject") @NotBlank(message = "subject" + ValidationMessage.NOT_BLANK) String subject,
                @JsonProperty("body") @NotBlank(message = "body" + ValidationMessage.NOT_BLANK) String body,
                @JsonProperty("sentAt") Instant sentAt) {
}