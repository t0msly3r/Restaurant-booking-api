package com.foodkin.restaurante.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class TableDTO {
    private String id;

    @NotNull(message = "table number is required")
    @Min(value = 1, message = "table number must be greater than 0")
    private Integer number;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;

    private Boolean available;
}
