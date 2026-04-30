package com.foodkin.restaurante.controller;

import com.foodkin.restaurante.dto.ReservationRequestDTO;
import com.foodkin.restaurante.dto.ReservationResponseDTO;
import com.foodkin.restaurante.model.Reservation;
import com.foodkin.restaurante.service.ReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(
            @Valid @RequestBody ReservationRequestDTO dto,
            @AuthenticationPrincipal String userEmail) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(userEmail, dto));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-reservations")
    public ResponseEntity<List<ReservationResponseDTO>> getMyReservations(
            @AuthenticationPrincipal String userEmail) {
        return ResponseEntity.ok(reservationService.getMyReservation(userEmail));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDTO> cancel(
            @PathVariable String id,
            @AuthenticationPrincipal String userEmail) {
        return ResponseEntity.ok(reservationService.cancel(id, userEmail));
    }

}
