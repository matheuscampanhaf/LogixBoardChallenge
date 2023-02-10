package com.logixboard.logixboardassesment.exceptions;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(final String errorMessage) {
        super(errorMessage);
    }
}
