package com.farmacia.farmacia_api.exception;

public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String field, String message) {
        super(String.format("Erro de validação no campo '%s': %s", field, message));
    }
}