package com.foodkin.restaurante.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestaurantException extends RuntimeException{
    private final HttpStatus status;

    public RestaurantException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
