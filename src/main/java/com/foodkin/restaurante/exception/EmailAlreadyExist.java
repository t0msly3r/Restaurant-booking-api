package com.foodkin.restaurante.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExist extends RestaurantException {
    public EmailAlreadyExist(String email) {
        super("El email '" + email + "' ya está registrado", HttpStatus.CONFLICT);
    }
}
