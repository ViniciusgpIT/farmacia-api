package com.farmacia.farmacia_api.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s não encontrado com ID: %d", resource, id));
    }
    
    public ResourceNotFoundException(String resource, String identifier) {
        super(String.format("%s não encontrado: %s", resource, identifier));
    }
}