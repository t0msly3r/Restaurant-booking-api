package com.foodkin.restaurante.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFound extends RestaurantException {
    public ResourceNotFound(String resource, String id) {
        super(resource + " con id '" + id + "' no encontrado", HttpStatus.NOT_FOUND);
    }
}
