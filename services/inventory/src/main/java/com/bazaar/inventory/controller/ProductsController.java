package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.ProductDTO;
import com.bazaar.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getById(productId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO product) {
        return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Object> updateProduct(@RequestBody ProductDTO product) {
        return new ResponseEntity<>(productService.update(product), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return new ResponseEntity<>("PRODUCT DELETED", HttpStatus.OK);
    }
}
