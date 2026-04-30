package com.foodkin.restaurante.exception;

import org.springframework.http.HttpStatus;

public class ReservationOverlapException extends RestaurantException{
    public ReservationOverlapException() {
        super("The table is already reserved for that time", HttpStatus.CONFLICT);
    }
}
