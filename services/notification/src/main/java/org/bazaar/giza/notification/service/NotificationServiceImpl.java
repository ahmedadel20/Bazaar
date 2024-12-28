package org.bazaar.giza.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.bazaar.giza.notification.dto.NotificationDto;
import org.bazaar.giza.notification.entity.Notification;
import org.bazaar.giza.notification.entity.NotificationStatus;
import org.bazaar.giza.notification.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender emailSender;
    private final NotificationRepository notificationRepository;

    @Value("${application.mail.sent.from}")
    private String fromEmail;

    @RabbitListener(queues = "transaction_notification_queue")
    public void processEmailMessage(NotificationDto notificationDto) {
        Notification notification = Notification.builder()
                .recipient(notificationDto.recipient())
                .subject(notificationDto.subject())
                .body(notificationDto.body())
                .sentAt(Instant.now())
                .status(NotificationStatus.PENDING)
                .build();

        try {
            // Send email
            sendEmail(notificationDto.recipient(), notificationDto.subject(), notificationDto.body());
            notification.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
        } finally {
            notificationRepository.save(notification);
        }
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setFrom(fromEmail);
        helper.setSubject(subject);
        helper.setText(body, true);
        emailSender.send(message);
    }
}
