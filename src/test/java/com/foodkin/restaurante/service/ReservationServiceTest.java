package com.foodkin.restaurante.service;

import com.foodkin.restaurante.dto.ReservationRequestDTO;
import com.foodkin.restaurante.dto.ReservationResponseDTO;
import com.foodkin.restaurante.exception.ResourceNotFound;
import com.foodkin.restaurante.exception.RestaurantException;
import com.foodkin.restaurante.model.Reservation;
import com.foodkin.restaurante.model.Table;
import com.foodkin.restaurante.model.User;
import com.foodkin.restaurante.repository.ReservationRepository;
import com.foodkin.restaurante.repository.TableRepository;
import com.foodkin.restaurante.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private TableRepository tableRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ReservationService reservationService;

    private Table table;
    private User user;
    private ReservationRequestDTO dto;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        table = new Table();
        table.setId("table1");
        table.setNumber(1);
        table.setCapacity(4);
        table.setAvailable(true);

        user = new User();
        user.setId("user1");
        user.setEmail("juan@gmail.com");

        dto = new ReservationRequestDTO();
        dto.setTableId("table1");
        dto.setDateTime(LocalDateTime.now().plusDays(1));
        dto.setPeopleNum(2);
        dto.setNotes("without gluten");

        reservation = new Reservation();
        reservation.setId("reservation1");
        reservation.setTableId("table1");
        reservation.setUserId("user1");
        reservation.setDateTime(dto.getDateTime()); // ← ahora dto ya no es null
        reservation.setPeopleNum(2);
        reservation.setState("PENDING");
        reservation.setNotes("without gluten");
    }

    @Test
    void create_withValidData_shouldCreateReservation() {
        when(tableRepository.findById("table1")).thenReturn(Optional.of(table));
        when(reservationRepository.findByTableIdAndDateTimeBetween(
                anyString(), any(), any())).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("juan@gmail.com")).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationResponseDTO response = reservationService.create("juan@gmail.com", dto);

        assertThat(response.getTableId()).isEqualTo("table1");
        assertThat(response.getState()).isEqualTo("PENDING");
        assertThat(response.getPeopleNum()).isEqualTo(2);
    }

    @Test
    void create_withInexistentTable_shouldThrowException() {
        when(tableRepository.findById("table1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create("juan@gmail.com", dto))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    void create_withoutEnoughCapacity_shouldThrowException() {
        dto.setPeopleNum(29);
        when(tableRepository.findById("table1")).thenReturn(Optional.of(table));

        assertThatThrownBy(() -> reservationService.create("juan@gmail.com", dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("seats");
    }

    @Test
    void create_withTableNotAvailable_shouldThrowException() {
        table.setAvailable(false);
        when(tableRepository.findById("table1")).thenReturn(Optional.of(table));

        assertThatThrownBy(() -> reservationService.create("juan@gmail.com", dto))
                .isInstanceOf(RestaurantException.class)
                .hasMessageContaining("available");
    }

    @Test
    void cancel_withOwnReservation_shouldCancel() {
        when(reservationRepository.findById("reservation1")).thenReturn(Optional.of(reservation));
        when(userRepository.findByEmail("juan@gmail.com")).thenReturn(Optional.of(user));
        when(tableRepository.findById("table1")).thenReturn(Optional.of(table));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        ReservationResponseDTO response = reservationService.cancel("reservation1", "juan@gmail.com");
        assertThat(response.getState()).isEqualTo("CANCELED");
    }

    @Test
    void cancel_reservationNotOwn_shouldThrowException() {
        User otherUser = new User();
        otherUser.setId("otherUser");
        otherUser.setEmail("other@gmail.com");

        when(reservationRepository.findById("reservation1")).thenReturn(Optional.of(reservation));
        when(userRepository.findByEmail("other@gmail.com")).thenReturn(Optional.of(otherUser));
        assertThatThrownBy(() -> reservationService.cancel("reservation1", "other@gmail.com"))
                .isInstanceOf(RestaurantException.class)
                .hasMessageContaining("not allowed");
    }
}

