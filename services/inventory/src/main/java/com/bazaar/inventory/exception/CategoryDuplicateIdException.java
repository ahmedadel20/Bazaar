package com.bazaar.inventory.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value= HttpStatus.CONFLICT, reason="There is a category with the same id")
public class CategoryDuplicateIdException extends RuntimeException {
    public CategoryDuplicateIdException(String message) {
        super(message);
    }
}
