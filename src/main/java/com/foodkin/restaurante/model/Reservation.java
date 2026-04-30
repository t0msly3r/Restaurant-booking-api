package com.foodkin.restaurante.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;

    private String userId;
    private String tableId;

    private LocalDateTime dateTime;
    private Integer peopleNum;
    private String state;
    private String notes;
}
