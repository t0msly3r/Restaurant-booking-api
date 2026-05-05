package com.foodkin.restaurante.service;

import com.foodkin.restaurante.dto.ReservationRequestDTO;
import com.foodkin.restaurante.dto.ReservationResponseDTO;
import com.foodkin.restaurante.exception.ReservationOverlapException;
import com.foodkin.restaurante.exception.ResourceNotFound;
import com.foodkin.restaurante.exception.RestaurantException;
import com.foodkin.restaurante.model.Reservation;
import com.foodkin.restaurante.model.Table;
import com.foodkin.restaurante.repository.ReservationRepository;
import com.foodkin.restaurante.repository.TableRepository;
import com.foodkin.restaurante.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class   ReservationService {
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;

    public ReservationResponseDTO create(String userEmail, ReservationRequestDTO dto) {
        // Check if table exists
        Table table = tableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new ResourceNotFound("Table", dto.getTableId()));
        // Check if table has enough capacity
        if (table.getCapacity() < dto.getPeopleNum()) {
            throw new IllegalArgumentException(
                    "The table only seats only  " + table.getCapacity() + " people"
            );
        }

        if (!table.getAvailable()) {
            throw new RestaurantException(
                    "The table " + table.getNumber() + " is not available",
                    HttpStatus.CONFLICT
            );
        }

        // Check overlapping
        LocalDateTime start = dto.getDateTime().minusHours(2);
        LocalDateTime end = dto.getDateTime().plusHours(2);
        List<Reservation> overlap = reservationRepository
                .findByTableIdAndDateTimeBetween(dto.getTableId(), start, end)
                .stream()
                .filter(r -> !r.getState().equals("CANCEL"))
                .toList();

        if (!overlap.isEmpty()) {
            throw new ReservationOverlapException();
        }
        // Get user's id by his email
        String userId = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new ResourceNotFound("User", userEmail))
                .getId();

        // Create and Save the reservation
        Reservation reservation = new Reservation();
        reservation.setUserId(reservation.getId());
        reservation.setTableId((dto.getTableId()));
        reservation.setPeopleNum(dto.getPeopleNum());
        reservation.setDateTime(dto.getDateTime());
        reservation.setState("SAVING");
        reservation.setNotes(dto.getNotes());

        return toDTO(reservationRepository.save(reservation), table.getNumber());
    }

    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(r -> {
                    Integer tableNum = tableRepository.findById(r.getTableId())
                            .map(Table::getNumber)
                            .orElse(null);
                    return toDTO(r, tableNum);
                })
                .toList();
    }

    public List<ReservationResponseDTO> getMyReservation(String userEmail) {
        String userId = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User", userEmail))
                .getId();
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(r -> {
                    Integer tableNumber = tableRepository.findById(r.getTableId())
                            .map(Table::getNumber)
                            .orElse(null);
                    return toDTO(r, tableNumber);
                })
                .toList();
    }

    public ReservationResponseDTO cancel(String id, String userEmail) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Reservation", id));

        String userId = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User", userEmail))
                .getId();
        // Check if the reservation owns to the user
        if (!reservation.getUserId().equals(userId)) {
            throw new RestaurantException(
                    "You are not allowed to cancel this reservation",
                    HttpStatus.FORBIDDEN            );
        }
        reservation.setState("CANCELED");
        Table table = tableRepository.findById(reservation.getTableId()).orElse(null);
        Integer tableNumber = table != null ? table.getNumber() : null;
        return toDTO(reservationRepository.save(reservation), tableNumber);
    }

    private ReservationResponseDTO toDTO(Reservation reservation, Integer tableNumber) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUserId());
        dto.setTableId(reservation.getTableId());
        dto.setDateTime(reservation.getDateTime());
        dto.setPeopleNum(reservation.getPeopleNum());
        dto.setState(reservation.getState());
        dto.setNotes(reservation.getNotes());
        return dto;
    }
}

