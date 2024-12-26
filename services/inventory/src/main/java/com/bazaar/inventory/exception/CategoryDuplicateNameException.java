package com.bazaar.inventory.exception;

public class CategoryDuplicateNameException extends RuntimeException{
    public CategoryDuplicateNameException(String message) {
        super(message);
    }
}
