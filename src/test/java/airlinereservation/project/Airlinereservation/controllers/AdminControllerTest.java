package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.services.AdminService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFlight() {
        Flight flight = new Flight();
        flight.setFlightCode("FL123");
        flight.setSource("New York");
        flight.setDestination("Los Angeles");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(5));
        flight.setAvailableSeats(100);
        flight.setStatus("AVAILABLE");

        when(adminService.createFlight(flight)).thenReturn(flight);

        ResponseEntity<Flight> response = adminController.createFlight(flight);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(flight, response.getBody());
        verify(adminService, times(1)).createFlight(flight);
    }

    @Test
    void testDeleteFlight() {
        Long flightId = 1L;

        doNothing().when(adminService).deleteFlight(flightId);

        ResponseEntity<Void> response = adminController.deleteFlight(flightId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(adminService, times(1)).deleteFlight(flightId);
    }

    @Test
    void testGetAllReservations() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        when(adminService.getAllReservations()).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = adminController.getAllReservations();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservations, response.getBody());
        verify(adminService, times(1)).getAllReservations();
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);

        when(adminService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = adminController.getAllUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    void testUpdateFlightStatus() {
        doNothing().when(adminService).updateFlightStatus();

        ResponseEntity<String> response = adminController.updateFlightStatus();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Flight statuses updated successfully", response.getBody());
        verify(adminService, times(1)).updateFlightStatus();
    }
}
