package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationLockService reservationLockService;

    @InjectMocks
    private ReservationService reservationService;

    private Flight testFlight;
    private User testUser;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testFlight = new Flight();
        testFlight.setId(1L);
        testFlight.setFlightCode("FL123");
        testFlight.setAvailableSeats(10);
        testFlight.setDepartureTime(LocalDateTime.now().plusDays(1));

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testReservation = new Reservation();
        testReservation.setId(1L);
        testReservation.setFlight(testFlight);
        testReservation.setUser(testUser);
        testReservation.setReservedSeats(2);
        testReservation.setReservationTime(LocalDateTime.now());
        testReservation.setStatus("CONFIRMED");

        when(flightRepository.findById(1L)).thenReturn(Optional.of(testFlight));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(reservationLockService.isSeatLocked(1L)).thenReturn(false);
    }

    @Test
    void testCreateReservation_Success() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation createdReservation = reservationService.createReservation(1L, 1L, 2);

        assertNotNull(createdReservation);
        assertEquals(2, createdReservation.getReservedSeats());
        assertEquals(testFlight, createdReservation.getFlight());
        assertEquals(testUser, createdReservation.getUser());

        verify(reservationLockService, times(1)).lockSeats(1L);
        verify(flightRepository, times(1)).save(any(Flight.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_Failure_NotEnoughSeats() {
        testFlight.setAvailableSeats(1);

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(1L, 1L, 2));

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_Failure_SeatsLocked() {
        when(reservationLockService.isSeatLocked(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> reservationService.createReservation(1L, 1L, 2));

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testDeleteReservation_Success() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(50);
    
        User user = new User();
        user.setId(1L);
    
        Reservation testReservation = new Reservation();
        testReservation.setId(1L);
        testReservation.setFlight(flight);
        testReservation.setUser(user);
        testReservation.setReservedSeats(2);
        testReservation.setStatus("CONFIRMED");

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).delete(testReservation);
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void testGetAllReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(testReservation));

        List<Reservation> reservations = reservationService.getAllReservations();

        assertEquals(1, reservations.size());
        assertEquals(testReservation, reservations.get(0));

        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGetReservationById_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Reservation foundReservation = reservationService.getReservationById(1L);

        assertNotNull(foundReservation);
        assertEquals(1L, foundReservation.getId());
        assertEquals("CONFIRMED", foundReservation.getStatus());

        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.getReservationById(1L));
    }

    @Test
    void testGetReservationsByFlight() {
        when(reservationRepository.findByFlightId(1L)).thenReturn(List.of(testReservation));

        List<Reservation> reservations = reservationService.getReservationsByFlight(1L);

        assertEquals(1, reservations.size());
        assertEquals(testReservation, reservations.get(0));

        verify(reservationRepository, times(1)).findByFlightId(1L);
    }

    @Test
    void testGetReservationsByUser() {
        when(reservationRepository.findByUser(testUser)).thenReturn(List.of(testReservation));

        List<Reservation> reservations = reservationService.getReservationsByUser(1L);

        assertEquals(1, reservations.size());
        assertEquals(testReservation, reservations.get(0));

        verify(reservationRepository, times(1)).findByUser(testUser);
    }

    @Test
    void testIsFlightAvailableForReservation_Success() {
        assertTrue(reservationService.isFlightAvailableForReservation(1L, 2));
    }

    @Test
    void testIsFlightAvailableForReservation_Failure_NotEnoughSeats() {
        testFlight.setAvailableSeats(1);
        assertFalse(reservationService.isFlightAvailableForReservation(1L, 2));
    }

    @Test
    void testUpdateReservation() {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setReservedSeats(3);
        updatedReservation.setStatus("PENDING");

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

        Reservation result = reservationService.updateReservation(1L, updatedReservation);

        assertEquals(3, result.getReservedSeats());
        assertEquals("PENDING", result.getStatus());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
}
