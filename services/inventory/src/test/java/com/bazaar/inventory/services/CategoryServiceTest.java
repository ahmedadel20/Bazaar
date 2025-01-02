package com.bazaar.inventory.services;

import static org.junit.jupiter.api.Assertions.*;

import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.exception.CategoryDuplicateNameException;
import com.bazaar.inventory.exception.CategoryInUseException;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.repo.CategoryRepository;
import com.bazaar.inventory.service.CategoryServiceImpl;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepo;
    @Mock
    private ProductService productService;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static Category existingCategory;
    private static Category newCategory;
    private static Product existingProduct;

    @BeforeEach
    public void init() {
        existingCategory = Category.builder().id(1L).name("Existing Category").build();
        newCategory = Category.builder().id(2L).name("New Category").build();
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
    void testFindingCategoryById() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.of(existingCategory));
        assertEquals(
                existingCategory,
                categoryService.getById(1L)
        );
    }

    @Test
    void testFindingNonExistingCategoryById() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.getById(1L)
        );
    }

    @Test
    void testFindingCategoryByName() {
        Mockito.when(categoryRepo.findByName("Existing Category")).thenReturn(Optional.of(existingCategory));
        assertEquals(
                existingCategory,
                categoryService.getByName("Existing Category")
        );
    }

    @Test
    void testFindingNonExistingCategoryByName() {
        Mockito.when(categoryRepo.findByName("New Category")).thenReturn(Optional.empty());
        assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.getByName("New Category")
        );
    }

    @Test
    void testCreatingNewCategory() {
        Mockito.when(categoryRepo.save(newCategory)).thenReturn(newCategory);
        assertEquals(
                newCategory,
                categoryService.create(newCategory)
        );
    }

    @Test
    void testCreatingExistingCategory() {
        newCategory.setName("Existing Category");
        Mockito.when(categoryRepo.findByName("Existing Category")).thenReturn(Optional.of(existingCategory));
        assertThrows(
                CategoryDuplicateNameException.class,
                () -> categoryService.create(newCategory)
        );
    }

    @Test
    void testUpdatingCategoryName() {
        Category updatedCategory = Category.builder().id(1L).name("Updated Category").build();
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepo.findByName("Updated Category")).thenReturn(Optional.empty());
        Mockito.when(categoryRepo.save(updatedCategory)).thenReturn(updatedCategory);
        assertEquals(
                updatedCategory,
                categoryService.update(updatedCategory)
        );
    }

    @Test
    void testUpdatingNonExistingCategory() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(categoryRepo.findByName("New Category")).thenReturn(Optional.empty());
        Mockito.when(categoryRepo.save(newCategory)).thenReturn(newCategory);
        assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.update(newCategory)
        );
    }

    @Test
    void testUpdatingCategoryWithDuplicateName() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepo.findByName("Existing Category")).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepo.save(existingCategory)).thenReturn(existingCategory);
        assertThrows(
                CategoryDuplicateNameException.class,
                () -> categoryService.update(existingCategory)
        );
    }

    @Test
    void testDeletingCategory() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.of(existingCategory));
        Mockito.when(productService.getProductsByCategory(existingCategory)).thenReturn(List.of());
        assertEquals(
                "CATEGORY DELETED",
                categoryService.delete(1L)
        );
    }

    @Test
    void testDeletingNonExistingCategory() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(productService.getProductsByCategory(existingCategory)).thenReturn(List.of());
        assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.delete(1L)
        );
    }

    @Test
    void testDeletingInUseCategory() {
        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.of(existingCategory));
        Mockito.when(productService.getProductsByCategory(existingCategory)).thenReturn(List.of(existingProduct));
        assertThrows(
                CategoryInUseException.class,
                () -> categoryService.delete(1L)
        );
    }
}
