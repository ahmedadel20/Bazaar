package com.bazaar.inventory.controller;

import com.bazaar.inventory.exception.ErrorResponse;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(ProductNotFoundException exc) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                exc.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFound(CategoryNotFoundException exc) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                exc.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid() {
//
//    }

}
