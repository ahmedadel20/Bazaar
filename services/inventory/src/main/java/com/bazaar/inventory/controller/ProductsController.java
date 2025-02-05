package com.bazaar.inventory.controller;

import com.bazaar.inventory.dto.PriceUpdateRequest;
import com.bazaar.inventory.dto.ProductDto;
import com.bazaar.inventory.dto.ProductMapper;
import com.bazaar.inventory.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Products", description = "Controller for handling mappings for products")
@Controller
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductsController {
    private ProductServiceImpl productService;
    private ProductMapper productMapper;

    @GetMapping()
    @ResponseBody
    public List<ProductDto> getProducts() {
        return productService
                .getAll()
                .stream()
                .map(product -> productMapper.toProductDTO(product))
                .toList();
    }

    @GetMapping("/{productId}")
    @ResponseBody
    public ProductDto getProductById(@PathVariable Long productId) {
        return productMapper.toProductDTO(productService.getById(productId));
    }

    @PutMapping("/bycategories")
    @ResponseBody
    public List<ProductDto> getProductsByCategories(@RequestBody List<Long> categoryIds) {
        return productService
                .getProductsByCategories(categoryIds)
                .stream()
                .map(productMapper::toProductDTO)
                .toList();
    }

    @PutMapping("/listofproducts")
    @ResponseBody
    public List<ProductDto> getListOfProducts(@RequestBody List<Long> productIds) {
        return productService
                .getProductsByIds(productIds)
                .stream()
                .map(product -> productMapper.toProductDTO(product))
                .toList();
    }

    @PostMapping()
    @ResponseBody
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return productMapper.toProductDTO(productService.create(productMapper.toProduct(productDto)));
    }

    @PutMapping()
    @ResponseBody
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        return productMapper.toProductDTO(productService.update(productMapper.toProduct(productDto)));
    }

    @PutMapping("/updateprices")
    @ResponseBody
    public String updateProductsPrices(@RequestBody PriceUpdateRequest priceUpdateRequest) {
        List<Long> productsIDs = priceUpdateRequest.productIds();
        float discount = priceUpdateRequest.discountPercentage();
        return productService.updateProductsPrices(productsIDs, discount);
    }

    @DeleteMapping("/{productId}")
    @ResponseBody
    public String deleteProduct(@PathVariable Long productId) {
        return productService.delete(productId);
    }
}
