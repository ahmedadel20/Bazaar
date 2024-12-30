package org.bazaar.giza.notification.mapper;

import org.bazaar.giza.notification.dto.NotificationDto;
import org.bazaar.giza.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public Notification toNotification(NotificationDto notificationDto) {
        return Notification.builder()
                .recipient(notificationDto.recipient())
                .subject(notificationDto.subject())
                .body(notificationDto.body())
                .sentAt(notificationDto.sentAt())
                .build();
    }
}
