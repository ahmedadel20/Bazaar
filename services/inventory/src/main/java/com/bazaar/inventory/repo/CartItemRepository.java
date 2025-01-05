package com.bazaar.inventory.repo;

import com.bazaar.inventory.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByBazaarUserId(Long bazaarUserId);

    List<CartItem> findAllByBazaarUserId(Long bazaarUserId);

    @NativeQuery("select * from cart_items where bazaar_user_id = ?1 and product_id = ?2")
    List<CartItem> findByBazaarUserIdAndProductId(Long bazaarUserId, Long productId);
}
