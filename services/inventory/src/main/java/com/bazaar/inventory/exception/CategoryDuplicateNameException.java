package com.bazaar.inventory.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value= HttpStatus.CONFLICT, reason="There is a category with the same name")
public class CategoryDuplicateNameException extends RuntimeException{
    public CategoryDuplicateNameException(String message) {
        super(message);
    }
}
