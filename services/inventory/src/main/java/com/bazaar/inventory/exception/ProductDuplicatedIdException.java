package com.bazaar.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Existing Product With Same ID")
public class ProductDuplicatedIdException extends RuntimeException {
    public ProductDuplicatedIdException(String message) {
        super(message);
    }
}
