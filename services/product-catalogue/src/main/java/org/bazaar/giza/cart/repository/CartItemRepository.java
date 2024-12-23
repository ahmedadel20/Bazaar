package org.bazaar.giza.cart.repository;

import org.bazaar.giza.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByBazaarUserId(Long bazaarUserId);

    List<CartItem> findAllByBazaarUserId(Long bazaarUserId);
}
