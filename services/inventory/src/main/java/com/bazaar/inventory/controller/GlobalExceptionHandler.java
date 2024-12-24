package com.bazaar.inventory.controller;

import com.bazaar.inventory.exception.CategoryErrorResponse;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.exception.ProductErrorResponse;
import com.bazaar.inventory.exception.ProductNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException exc) {
        return new ResponseEntity<>(
                new ProductErrorResponse(
                        HttpStatus.NOT_FOUND,
                        exc.getMessage(),
                        new Timestamp(System.currentTimeMillis())
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFound(CategoryNotFoundException exc) {
        return new ResponseEntity<>(
                new CategoryErrorResponse(
                        HttpStatus.NOT_FOUND,
                        exc.getMessage(),
                        new Timestamp(System.currentTimeMillis())
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
