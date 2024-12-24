package org.bazaar.giza.cartItem.repository;

import org.bazaar.giza.cartItem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByBazaarUserId(Long bazaarUserId);

    List<CartItem> findAllByBazaarUserId(Long bazaarUserId);
}
