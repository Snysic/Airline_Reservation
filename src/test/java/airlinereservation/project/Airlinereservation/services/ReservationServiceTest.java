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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllReservations() {
        when(reservationRepository.findAll()).thenAnswer(invocation -> Arrays.asList(new Reservation(), new Reservation()));

        var reservations = reservationService.getAllReservations();

        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testGetReservationById_Success() {
        when(reservationRepository.findById(1L)).thenAnswer(invocation -> Optional.of(new Reservation() {{ setId(1L); }}));

        var result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetReservationById_NotFound() {
        when(reservationRepository.findById(1L)).thenAnswer(invocation -> Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> reservationService.getReservationById(1L));

        assertEquals("Reservation not found with ID: 1", exception.getMessage());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateReservation_Success() {
        Flight flight = new Flight() {{ setId(1L); setAvailableSeats(100); setFlightCode("FL123"); }};
        User user = new User() {{ setId(1L); }};
        Reservation reservation = new Reservation() {{ setReservedSeats(2); }};

        when(flightRepository.findById(1L)).thenAnswer(invocation -> Optional.of(flight));
        when(userRepository.findById(1L)).thenAnswer(invocation -> Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> reservation);

        var result = reservationService.createReservation(1L, 1L, 2);

        assertNotNull(result);
        assertEquals(2, result.getReservedSeats());
        assertEquals(98, flight.getAvailableSeats());
        verify(flightRepository, times(1)).save(flight);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testCreateReservation_NotEnoughSeats() {
        Flight flight = new Flight() {{ setId(1L); setAvailableSeats(1); }};
        User user = new User() {{ setId(1L); }};

        when(flightRepository.findById(1L)).thenAnswer(invocation -> Optional.of(flight));
        when(userRepository.findById(1L)).thenAnswer(invocation -> Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(1L, 1L, 2));

        assertEquals("Not enough seats available for flight: null", exception.getMessage());
        verify(flightRepository, never()).save(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    public void testUpdateReservation_Success() {
        Flight flight = new Flight() {{ setAvailableSeats(10); }};
        Reservation existingReservation = new Reservation() {{ setId(1L); setReservedSeats(2); setFlight(flight); }};

        Reservation updatedReservation = new Reservation() {{ setReservedSeats(4); }};

        when(reservationRepository.findById(1L)).thenAnswer(invocation -> Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> updatedReservation);

        var result = reservationService.updateReservation(1L, updatedReservation);

        assertNotNull(result);
        assertEquals(4, result.getReservedSeats());
        assertEquals(8, flight.getAvailableSeats());
        verify(flightRepository, times(1)).save(flight);
        verify(reservationRepository, times(1)).save(existingReservation);
    }

    @Test
    public void testDeleteReservation_Success() {
        Flight flight = new Flight() {{ setAvailableSeats(5); }};

        Reservation reservation = new Reservation() {{ setId(1L); setReservedSeats(2); setFlight(flight); }};

        when(reservationRepository.findById(1L)).thenAnswer(invocation -> Optional.of(reservation));

        reservationService.deleteReservation(1L);

        assertEquals(7, flight.getAvailableSeats());
        verify(flightRepository, times(1)).save(flight);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    public void testIsFlightAvailableForReservation_True() {
        when(flightRepository.findById(1L)).thenAnswer(invocation -> Optional.of(new Flight() {{ setAvailableSeats(10); }}));

        boolean result = reservationService.isFlightAvailableForReservation(1L, 5);

        assertTrue(result);
    }

    @Test
    public void testIsFlightAvailableForReservation_False() {
        when(flightRepository.findById(1L)).thenAnswer(invocation -> Optional.of(new Flight() {{ setAvailableSeats(3); }}));

        boolean result = reservationService.isFlightAvailableForReservation(1L, 5);

        assertFalse(result);
    }
}
