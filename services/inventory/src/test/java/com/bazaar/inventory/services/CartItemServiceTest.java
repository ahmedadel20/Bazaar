package com.bazaar.inventory.services;

import com.bazaar.inventory.entity.CartItem;
import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.exception.CartItemNotFoundException;
import com.bazaar.inventory.repo.CartItemRepository;
import com.bazaar.inventory.service.CartItemServiceImpl;
import com.bazaar.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CartItemServiceTest {
        @Mock
        private CartItemRepository cartItemRepo;
        @Mock
        private ProductService productService;
        @Mock
        private RabbitTemplate rabbitTemplate;
        @InjectMocks
        private CartItemServiceImpl cartItemService;

        private static CartItem existingCartItem;
        private static CartItem newCartItem;
        private static Category existingCategory;
        private static Product product1;
        private static Product product2;

        @BeforeEach
        public void init() {
                existingCategory = Category.builder().id(1L).name("Existing Category").build();
                product1 = Product.builder()
                                .id(1L)
                                .productCategory(existingCategory)
                                .name("Test Product 1")
                                .quantity(50L)
                                .lastUpdated(new Timestamp(System.currentTimeMillis()))
                                .originalPrice(BigDecimal.TEN)
                                .currentPrice(BigDecimal.TEN)
                                .build();
                product2 = Product.builder()
                                .id(2L)
                                .productCategory(existingCategory)
                                .name("Test Product 2")
                                .quantity(100L)
                                .lastUpdated(new Timestamp(System.currentTimeMillis()))
                                .originalPrice(BigDecimal.ONE)
                                .currentPrice(BigDecimal.ONE)
                                .build();
                existingCartItem = CartItem.builder()
                                .id(1L)
                                .bazaarUserId(1L)
                                .cartProduct(product1)
                                .quantity(1)
                                .build();
                newCartItem = CartItem.builder()
                                .id(2L)
                                .bazaarUserId(1L)
                                .cartProduct(product2)
                                .quantity(2)
                                .build();
        }

        @Test
        void testFindCartItem() {
                Mockito.when(cartItemRepo.findById(1L)).thenReturn(Optional.of(existingCartItem));
                assertEquals(
                                existingCartItem,
                                cartItemService.getItem(1L));
        }

        @Test
        void testFindNonExistingCartItem() {
                Mockito.when(cartItemRepo.findById(1L)).thenReturn(Optional.empty());
                assertThrows(
                                CartItemNotFoundException.class,
                                () -> cartItemService.getItem(1L));
        }

        @Test
        void testAddingNewCartItem() {
                Mockito.when(productService.getById(2L)).thenReturn(product2);
                Mockito.when(cartItemRepo.findByBazaarUserIdAndProductId(1L, 2L))
                                .thenReturn(List.of());
                Mockito.when(cartItemRepo.save(newCartItem)).thenReturn(newCartItem);
                assertEquals(
                                newCartItem,
                                cartItemService.addItem(newCartItem));
        }

        @Test
        void testAddingExistingCartItem() {
                Mockito.when(productService.getById(existingCartItem.getCartProduct().getId()))
                                .thenReturn(product1);
                Mockito.when(
                                cartItemRepo
                                                .findByBazaarUserIdAndProductId(
                                                                existingCartItem.getBazaarUserId(),
                                                                existingCartItem.getCartProduct().getId()))
                                .thenReturn(List.of(existingCartItem));

                Mockito.when(cartItemRepo.save(existingCartItem)).thenReturn(existingCartItem);
                assertEquals(
                                existingCartItem.getQuantity() * 2,
                                cartItemService.addItem(existingCartItem).getQuantity());
        }

        @Test
        void testUpdateItemQuantity() {
                Mockito.when(cartItemRepo.findById(existingCartItem.getId()))
                                .thenReturn(Optional.of(existingCartItem));
                Mockito.when(cartItemRepo.save(existingCartItem))
                                .thenReturn(existingCartItem);
                var returnedItem = cartItemService.updateItemQuantity(existingCartItem.getId(), 2);
                assertEquals(
                                existingCartItem,
                                returnedItem);
                assertEquals(
                                2,
                                returnedItem.getQuantity());
        }

        @Test
        void testUpdateQuantityOfNonExistingItem() {
                Mockito.when(cartItemRepo.findById(newCartItem.getId()))
                                .thenReturn(Optional.empty());
                Mockito.when(cartItemRepo.save(newCartItem))
                                .thenReturn(newCartItem);
                assertThrows(
                                CartItemNotFoundException.class,
                                () -> cartItemService.updateItemQuantity(newCartItem.getId(), 2));
        }

        @Test
        void testUpdateProductOfCartItem() {
                Mockito.when(cartItemRepo.findById(existingCartItem.getId()))
                                .thenReturn(Optional.of(existingCartItem));
                Mockito.when(cartItemRepo.save(existingCartItem))
                                .thenReturn(existingCartItem);
                Mockito.when(productService.getById(2L)).thenReturn(product2);
                existingCartItem.getCartProduct().setId(2L);
                var returnedItem = cartItemService.updateItem(existingCartItem);

                assertEquals(
                                existingCartItem,
                                returnedItem);
                assertEquals(
                                product2.getCurrentPrice(),
                                returnedItem.getCartProduct().getCurrentPrice());
        }

        @Test
        void testUpdateProductOfNonExistingCartItem() {
                Mockito.when(cartItemRepo.findById(newCartItem.getId()))
                                .thenReturn(Optional.empty());
                Mockito.when(cartItemRepo.save(newCartItem))
                                .thenReturn(newCartItem);
                Mockito.when(productService.getById(2L)).thenReturn(product2);
                assertThrows(
                                CartItemNotFoundException.class,
                                () -> cartItemService.updateItem(newCartItem));
        }

        @Test
        void testDeleteCartItem() {
                Mockito.when(cartItemRepo.existsById(existingCartItem.getId())).thenReturn(true);
                assertEquals(
                                "Item removed",
                                cartItemService.removeItem(existingCartItem.getId()));
        }

        @Test
        void testDeleteNonExistingItem() {
                Mockito.when(cartItemRepo.existsById(newCartItem.getId())).thenReturn(false);
                assertThrows(
                                CartItemNotFoundException.class,
                                () -> cartItemService.removeItem(newCartItem.getId()));
        }
}
