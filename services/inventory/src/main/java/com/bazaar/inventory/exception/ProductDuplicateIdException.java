package com.bazaar.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Existing Product With Same ID")
public class ProductDuplicateIdException extends RuntimeException {
    public ProductDuplicateIdException(String message) {
        super(message);
    }
}
