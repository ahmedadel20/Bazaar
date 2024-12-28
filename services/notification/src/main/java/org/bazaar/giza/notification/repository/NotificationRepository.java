package org.bazaar.giza.notification.repository;

import org.bazaar.giza.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
