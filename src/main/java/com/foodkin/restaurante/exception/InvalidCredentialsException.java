package com.foodkin.restaurante.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends RestaurantException {
    public InvalidCredentialsException () {
        super("Email or password incorrect", HttpStatus.UNAUTHORIZED);
    }
}
