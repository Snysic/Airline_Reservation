package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Flight flight = new Flight("FL123", "New York", "Los Angeles",
                LocalDateTime.of(2025, 1, 17, 8, 0),
                LocalDateTime.of(2025, 1, 17, 11, 0), 100, "AVAILABLE");
        flight.setId(1L);

        User user = new User("user", "password", "user@example.com");
        user.setId(1L);

        reservation = new Reservation(flight, user, LocalDateTime.now(), 2, "CONFIRMED");
        reservation.setId(1L);
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.getAllReservations()).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = reservationController.getAllReservations();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservations);
        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void testGetReservationById() {
        when(reservationService.getReservationById(1L)).thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.getReservationById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservation);
        verify(reservationService, times(1)).getReservationById(1L);
    }

    @Test
    void testCreateReservation() {
        when(reservationService.createReservation(1L, 1L, 2)).thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.createReservation(reservation);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(reservation);
        verify(reservationService, times(1))
                .createReservation(reservation.getFlight().getId(), reservation.getUser().getId(), reservation.getReservedSeats());
    }

    @Test
    void testUpdateReservation() {
        Reservation updatedReservation = new Reservation(
                reservation.getFlight(),
                reservation.getUser(),
                LocalDateTime.now(),
                3,
                "PENDING");
        updatedReservation.setId(1L);

        when(reservationService.updateReservation(1L, updatedReservation)).thenReturn(updatedReservation);

        ResponseEntity<Reservation> response = reservationController.updateReservation(1L, updatedReservation);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedReservation);
        verify(reservationService, times(1)).updateReservation(1L, updatedReservation);
    }

    @Test
    void testDeleteReservation() {
        ResponseEntity<Void> response = reservationController.deleteReservation(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(reservationService, times(1)).deleteReservation(1L);
    }
}
