package org.bazaar.giza.order.repository;

import org.bazaar.giza.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBazaarUserId(Long bazaarUserId);
}