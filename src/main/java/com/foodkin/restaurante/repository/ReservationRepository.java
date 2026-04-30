package com.foodkin.restaurante.repository;

import com.foodkin.restaurante.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByUserId(String userId);
    List<Reservation> findByTableId(String tableId);
    List<Reservation> findByState(String state);

    List<Reservation> findByTableIdAndDateTimeBetween(
            String tableId,
            LocalDateTime start,
            LocalDateTime end
    );
}

