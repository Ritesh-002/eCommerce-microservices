package com.ritesh.product.exceptions;

public class InsufficientStockException extends  RuntimeException{
    public InsufficientStockException(String message) {
        super(message);
    }
}
