package com.bazaar.inventory.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.exception.ProductNotFoundException;
import com.bazaar.inventory.repo.ProductRepository;

import com.bazaar.inventory.service.CategoryService;
import com.bazaar.inventory.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private static Product existingProduct;
    private static Category existingCategory;

    @BeforeEach
    public void init() {
        existingCategory = Category.builder().id(1L).name("testCategory").build();
        existingProduct = Product.builder()
                .id(1L)
                .productCategory(existingCategory)
                .name("testProduct")
                .quantity(50L)
                .lastUpdated(new Timestamp(System.currentTimeMillis()))
                .originalPrice(BigDecimal.TEN)
                .currentPrice(BigDecimal.TEN)
                .build();

    }


    @Test
    void testFindingProduct() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(existingProduct));
        Product product = productService.getById(1L);
        assertNotEquals(null, product);
        assertEquals(1L, product.getId());
    }

    @Test
    void testFindingNonExistingProduct() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getById(1L)
        );
    }

    @Test
    void testFindingProductsByCategories() {
        Mockito.when(productRepo.findByProductCategory(existingCategory)).thenReturn(List.of(existingProduct));
        Mockito.when(categoryService.getById(1L)).thenReturn(existingCategory);
        assertEquals(List.of(existingProduct), productService.getProductsByCategories(List.of(1L)));
    }

    @Test
    void testFindingProductsByIds() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(existingProduct));
        assertEquals(List.of(existingProduct), productService.getProductsByIds(List.of(1L)));
    }

    @Test
    void testCreatingProduct() {
        Mockito.when(productRepo.save(Mockito.any(Product.class))).thenReturn(existingProduct);
        Mockito.when(categoryService.getById(1L)).thenReturn(existingCategory);
        Product returnedProduct = productService.create(existingProduct);
        assertNotEquals(null, returnedProduct);
    }

    @Test
    void testCreatingProductWithNonExistingCategory() {
        Mockito.doThrow(CategoryNotFoundException.class).when(categoryService).getById(1L);
        assertThrows(
                CategoryNotFoundException.class,
                () -> productService.create(existingProduct)
        );
    }

    @Test
    void testUpdatingProduct() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepo.save(Mockito.any(Product.class))).thenReturn(existingProduct);
        assertEquals(existingProduct, productService.update(existingProduct));
    }

    @Test
    void testUpdatingNonExistingProduct() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.update(existingProduct)
        );
    }

    @Test
    void testUpdatingProductsPrices() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(existingProduct));
        productService.updateProductsPrices(List.of(1L), 90.0);
        assertEquals(
                0,
                existingProduct.getCurrentPrice().compareTo(BigDecimal.ONE)
        );
    }

    @Test
    void testDeletingProduct() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(existingProduct));
        assertEquals(
                "PRODUCT DELETED",
                productService.delete(1L)
        );
    }

    @Test
    void testDeletingNonExistingProduct() {
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.delete(1L)
        );
    }

}
