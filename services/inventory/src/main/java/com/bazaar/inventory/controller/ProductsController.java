package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.ProductDTO;
import com.bazaar.inventory.dto.ProductMapper;
import com.bazaar.inventory.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductsController {
    private ProductServiceImpl productService;
    private ProductMapper productMapper;

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
        productService.delete(productId);
        return "PRODUCT DELETED";
    }
}
