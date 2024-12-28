package org.bazaar.giza.notification.service;

import jakarta.mail.MessagingException;
import org.bazaar.giza.notification.dto.NotificationDto;

public interface NotificationService {
    public void processEmailMessage(NotificationDto notificationDto);
    public void sendEmail(String to, String subject, String body) throws MessagingException;
}
