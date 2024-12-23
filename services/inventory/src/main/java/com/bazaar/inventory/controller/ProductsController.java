package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.ProductDTO;
import jakarta.validation.Valid;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/products")
public class ProductsController {
    private ProductService productService;

    @Autowired
    public ProductsController (ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<Object> getProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Object> updateProduct(@RequestBody ProductDTO product) {
        return new ResponseEntity<>(productService.updateProduct(product), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("PRODUCT DELETED", HttpStatus.OK);
    }
}
