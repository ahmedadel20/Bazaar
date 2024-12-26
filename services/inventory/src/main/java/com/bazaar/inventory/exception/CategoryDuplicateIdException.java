package com.bazaar.inventory.exception;

public class CategoryDuplicateIdException extends RuntimeException {
    public CategoryDuplicateIdException(String message) {
        super(message);
    }
}
