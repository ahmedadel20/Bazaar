package org.bazaar.giza.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.bazaar.giza.notification.dto.NotificationDto;
import org.bazaar.giza.notification.entity.Notification;
import org.bazaar.giza.notification.entity.NotificationStatus;
import org.bazaar.giza.notification.mapper.NotificationMapper;
import org.bazaar.giza.notification.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender emailSender;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Value("${application.mail.sent.from}")
    private String fromEmail;

    @RabbitListener(queues = {"transaction_notification_queue", "cart_notification_queue"})
    public void processNotification(NotificationDto notificationDto) {
        try {
            // Send email
            sendEmail(notificationDto);
        } catch (Exception e) {
            Notification notification = notificationMapper.toNotification(notificationDto);
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
        }
    }

    public void sendEmail(NotificationDto notificationDto) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(notificationDto.recipient());
        helper.setFrom(fromEmail);
        helper.setSubject(notificationDto.subject());
        helper.setText(notificationDto.body(), true);
        emailSender.send(message);
    }
}
