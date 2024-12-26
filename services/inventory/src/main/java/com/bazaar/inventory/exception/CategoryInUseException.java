package com.bazaar.inventory.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value= HttpStatus.CONFLICT, reason="Category is in use by products")
public class CategoryInUseException extends RuntimeException{
    public CategoryInUseException(String message) {
        super(message);
    }
}
