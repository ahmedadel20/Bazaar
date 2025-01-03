package org.bazaar.giza.services;

import org.aspectj.weaver.ast.Not;
import org.bazaar.giza.notification.dto.NotificationDto;
import org.bazaar.giza.notification.entity.Notification;
import org.bazaar.giza.notification.mapper.NotificationMapper;
import org.bazaar.giza.notification.repository.NotificationRepository;
import org.bazaar.giza.notification.service.NotificationServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NotificationServiceTest {
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private NotificationRepository notificationRepo;
    @Mock
    private NotificationMapper notificationMapper;
    @InjectMocks
    private NotificationServiceImpl notificationService;
    private static Notification notification;
    private static NotificationDto notificationDto;
    @BeforeEach
    void setup() {
        NotificationDto notificationDto = NotificationDto.builder()
                .recipient("Ahmed")
                .subject("Cart Update")
                .body("Checkout Your Cart")
                .build();
        Notification notification = Notification.builder()
                .id(1L)
                .recipient("Ahmed")
                .subject("Cart Update")
                .body("Checkout Your Cart")
                .build();
        Notification sentNotification = Notification.builder()
                .id(1L)
                .recipient("Ahmed")
                .subject("Cart Update")
                .body("Checkout Your Cart")
                .build();
        Notification failedNotification = Notification.builder()
                .id(1L)
                .recipient("Ahmed")
                .subject("Cart Update")
                .body("Checkout Your Cart")
                .build();

    }


}
