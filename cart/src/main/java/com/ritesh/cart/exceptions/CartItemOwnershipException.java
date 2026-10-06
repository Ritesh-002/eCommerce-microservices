package com.ritesh.cart.exceptions;

public class CartItemOwnershipException extends RuntimeException {

    public CartItemOwnershipException(String message) {
        super(message);
    }
}