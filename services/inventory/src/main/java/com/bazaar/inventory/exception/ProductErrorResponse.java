package com.bazaar.inventory.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ProductErrorResponse {
    private HttpStatus httpStatus;
    private String message;
    private Timestamp timeStamp;
}
