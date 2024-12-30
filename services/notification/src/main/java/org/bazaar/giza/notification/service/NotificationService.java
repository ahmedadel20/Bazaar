package org.bazaar.giza.notification.service;

import jakarta.mail.MessagingException;
import org.bazaar.giza.notification.dto.NotificationDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface NotificationService {
    @RabbitListener(queues = {"transaction_notification_queue", "cart_notification_queue"})
    void processNotification(NotificationDto notificationDto);
    void sendEmail(NotificationDto notificationDto) throws MessagingException;
}
