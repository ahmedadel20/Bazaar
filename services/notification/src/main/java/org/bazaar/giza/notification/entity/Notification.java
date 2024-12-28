package org.bazaar.giza.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "recipient", nullable = false)
    private String recipient;
    @Column(name = "subject", nullable = false)
    private String subject;
    @Column(name = "body", nullable = false)
    private String body; // Email body (HTML/Text)
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status; // SENT, FAILED, PENDING
}
