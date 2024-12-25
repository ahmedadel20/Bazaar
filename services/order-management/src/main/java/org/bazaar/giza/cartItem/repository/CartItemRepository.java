package org.bazaar.giza.cartItem.repository;

import org.bazaar.giza.cartItem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByBazaarUserId(Long bazaarUserId);

    List<CartItem> findAllByBazaarUserId(Long bazaarUserId);

    Optional<CartItem> findByBazaarUserIdAndProductId(Long bazaarUserId, Long productId);
}
