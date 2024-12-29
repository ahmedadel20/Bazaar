package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.CategoryDTO;
import com.bazaar.inventory.dto.CategoryMapper;
import com.bazaar.inventory.dto.ProductDTO;
import com.bazaar.inventory.dto.ProductMapper;
import com.bazaar.inventory.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductsController {
    private ProductServiceImpl productService;
    private ProductMapper productMapper;
    private CategoryMapper categoryMapper;

    @GetMapping()
    @ResponseBody
    public List<ProductDTO> getProducts() {
        return productService
                .getAll()
                .stream()
                .map(product -> productMapper.toProductDTO(product))
                .toList();
    }

    @GetMapping("/{productId}")
    @ResponseBody
    public ProductDTO getProductById(@PathVariable Long productId) {
        return productMapper.toProductDTO(productService.getById(productId));
    }

    @GetMapping("/bycategories")
    @ResponseBody
    public List<ProductDTO> getProductById(@RequestBody List<CategoryDTO> categoryDTOs) {
        return productService
                .getProductsByCategories(
                    categoryDTOs
                            .stream()
                            .map(categoryMapper::toCategory)
                            .toList()
                )
                .stream()
                .map(productMapper::toProductDTO)
                .toList();
    }

    @GetMapping("/listofproducts")
    @ResponseBody
    public List<ProductDTO> getListOfProducts(@RequestBody List<Long> productIds) {
        return productService
                .getProductsByIds(productIds)
                .stream()
                .map(product -> productMapper.toProductDTO(product))
                .toList();
    }

    @PostMapping()
    @ResponseBody
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDto) {
        return productMapper.toProductDTO(productService.create(productMapper.toProduct(productDto)));
    }

    @PutMapping()
    @ResponseBody
    public ProductDTO updateProduct(@RequestBody @Valid ProductDTO productDto) {
        return productMapper.toProductDTO(productService.update(productMapper.toProduct(productDto)));
    }

    @DeleteMapping("/{productId}")
    @ResponseBody
    public String deleteProduct(@PathVariable Long productId) {
        return productService.delete(productId);
    }
}
