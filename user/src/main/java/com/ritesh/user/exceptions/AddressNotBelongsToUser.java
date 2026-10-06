package com.ritesh.user.exceptions;

public class AddressNotBelongsToUser extends RuntimeException{
    public AddressNotBelongsToUser(String message) {
        super(message);
    }
}
