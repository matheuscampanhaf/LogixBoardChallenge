package com.logixboard.logixboardassesment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFound.class})
    public ResponseEntity<Object> handleResourceNotFound(final ResourceNotFound exception) {
        return new ResponseEntity<>(
                new ApiException(
                        exception.getMessage(),
                        HttpStatus.NOT_FOUND
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return new ResponseEntity<>(
                new ApiException(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
