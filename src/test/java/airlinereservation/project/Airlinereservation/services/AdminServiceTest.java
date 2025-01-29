package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFlight_Success() {
        Flight flight = new Flight("FL123", "New York", "Los Angeles",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(5), 100, "Scheduled");

        when(flightRepository.save(flight)).thenReturn(flight);

        Flight createdFlight = adminService.createFlight(flight);

        assertNotNull(createdFlight);
        assertEquals("FL123", createdFlight.getFlightCode());
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testCreateFlight_InvalidData_ThrowsException() {
        Flight flight = new Flight();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.createFlight(flight));

        assertEquals("Flight data is invalid: Flight code is required", exception.getMessage());
        verify(flightRepository, never()).save(any());
    }

    @Test
    void testDeleteFlight_Success() {
        Long flightId = 1L;

        when(flightRepository.existsById(flightId)).thenReturn(true);

        adminService.deleteFlight(flightId);

        verify(flightRepository, times(1)).deleteById(flightId);
    }

    @Test
    void testDeleteFlight_FlightNotFound_ThrowsException() {
        Long flightId = 1L;

        when(flightRepository.existsById(flightId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.deleteFlight(flightId));

        assertEquals("Flight not found with ID: 1", exception.getMessage());
        verify(flightRepository, never()).deleteById(flightId);
    }

    @Test
    void testGetAllReservations_Success() {
        Reservation reservation = new Reservation();
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Reservation> reservations = adminService.getAllReservations();

        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGetAllReservations_NoReservationsFound_ThrowsException() {
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> adminService.getAllReservations());

        assertEquals("No reservations found", exception.getMessage());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_Success() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = adminService.getAllUsers();

        assertNotNull(users);
        assertFalse(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_NoUsersFound_ThrowsException() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> adminService.getAllUsers());

        assertEquals("No users found", exception.getMessage());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateFlightStatus_Success() {
        Flight flight1 = new Flight("FL001", "A", "B", LocalDateTime.now().minusHours(1), LocalDateTime.now(), 0, "Completed");
        Flight flight2 = new Flight("FL002", "C", "D", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(5), 5, "Scheduled");
        flight1.setAvailable(true);
        flight2.setAvailable(true);

        when(flightRepository.findAll()).thenReturn(List.of(flight1, flight2));
        when(flightRepository.save(flight1)).thenReturn(flight1);

        adminService.updateFlightStatus();

        assertFalse(flight1.isAvailable());
        assertTrue(flight2.isAvailable());
        verify(flightRepository, times(1)).save(flight1);
    }
}
