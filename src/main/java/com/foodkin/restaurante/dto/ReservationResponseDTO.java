package com.foodkin.restaurante.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {
    private String id;
    private String userId;
    private String tableId;
    private Integer tableNumber;
    private LocalDateTime dateTime;
    private Integer peopleNum;
    private String state;
    private String notes;
}
