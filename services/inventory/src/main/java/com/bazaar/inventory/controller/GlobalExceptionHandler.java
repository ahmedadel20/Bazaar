package com.bazaar.inventory.controller;

import com.bazaar.inventory.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(CategoryDuplicateNameException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryDuplicateName(CategoryDuplicateNameException exc) {
        return new ErrorResponse(
                HttpStatus.CONFLICT,
                exc.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(CategoryInUseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoryInUse(CategoryInUseException exc) {
        return new ErrorResponse(
                HttpStatus.CONFLICT,
                exc.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCartItemNotFoundException(CartItemNotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }


    @ExceptionHandler(InvalidQuantityException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public ErrorResponse handleInvalidQuantityException(InvalidQuantityException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }




//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid() {
//
//    }

}
