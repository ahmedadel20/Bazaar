package org.bazaar.giza.errorHandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String message;
    private Timestamp timeStamp;

    public ErrorResponse(String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.message = message;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }
}
