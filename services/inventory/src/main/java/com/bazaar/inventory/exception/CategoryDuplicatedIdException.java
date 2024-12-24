package com.bazaar.inventory.exception;

public class CategoryDuplicatedIdException extends RuntimeException {
    public CategoryDuplicatedIdException(String message) {
        super(message);
    }
}
