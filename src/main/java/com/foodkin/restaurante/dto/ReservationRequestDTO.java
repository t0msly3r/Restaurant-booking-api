package com.foodkin.restaurante.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {
    @NotBlank(message = "Table id is required")
    private String tableId;

    @NotNull(message = "Date and time are required")
    @Future(message = "Reservation must be in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "People number b")
    @Min(value = 1, message = "Must be 1 person at least")
    private Integer peopleNum;

    private String notes;
}
