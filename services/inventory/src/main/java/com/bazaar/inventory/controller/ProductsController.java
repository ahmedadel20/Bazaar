package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.CategoryDTO;
import com.bazaar.inventory.dto.CategoryMapper;
import com.bazaar.inventory.dto.ProductDTO;
import com.bazaar.inventory.dto.ProductMapper;
import com.bazaar.inventory.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getProducts() {
        return productService
                .getAll()
                .stream()
                .map(product -> productMapper.toProductDTO(product))
                .toList();
    }

    @GetMapping("/{productId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductById(@PathVariable Long productId) {
        return productMapper.toProductDTO(productService.getById(productId));
    }

    @GetMapping("/bycategory")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getProductById(@RequestBody CategoryDTO categoryDto) {
        return productService.getProductsByCategory(categoryMapper.toCategory(categoryDto))
            .stream()
            .map(productMapper::toProductDTO)
            .toList();
    }

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDto) {
        return productMapper.toProductDTO(productService.create(productMapper.toProduct(productDto)));
    }

    @PutMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO updateProduct(@RequestBody @Valid ProductDTO productDto) {
        return productMapper.toProductDTO(productService.update(productMapper.toProduct(productDto)));
    }

    @DeleteMapping("/{productId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String deleteProduct(@PathVariable Long productId) {
        return productService.delete(productId);
    }
}
