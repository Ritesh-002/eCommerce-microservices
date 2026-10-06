package com.ritesh.user.exceptions;

public class UserAddressNotFoundException extends RuntimeException{
    public UserAddressNotFoundException(String message) {
        super(message);
    }
}
