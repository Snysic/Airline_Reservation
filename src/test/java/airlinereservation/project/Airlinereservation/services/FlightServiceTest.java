package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFlights() {
        List<Flight> flights = List.of(new Flight());
        when(flightRepository.findAll()).thenReturn(flights);

        List<Flight> result = flightService.getAllFlights();

        assertEquals(flights, result);
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    public void testGetFlightById_FlightExists() {
        Flight flight = new Flight();
        flight.setId(1L);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Flight result = flightService.getFlightById(1L);

        assertEquals(flight, result);
        verify(flightRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetFlightById_FlightNotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> flightService.getFlightById(1L));

        assertEquals("Flight not found with ID: 1", exception.getMessage());
        verify(flightRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateFlight_Successful() {
        Flight flight = new Flight("FL123", "NYC", "LAX", LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(8), 100, "AVAILABLE");

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        Flight result = flightService.createFlight(flight);

        assertEquals(flight, result);
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    public void testCreateFlight_InvalidDepartureTime() {
        Flight flight = new Flight("FL123", "NYC", "LAX", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(2), 100, "AVAILABLE");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> flightService.createFlight(flight));

        assertEquals("Cannot create a flight with a past departure time.", exception.getMessage());
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    public void testUpdateFlight_Successful() {
        Flight existingFlight = new Flight("FL123", "NYC", "LAX", LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(8), 100, "AVAILABLE");
        existingFlight.setId(1L);

        Flight updatedFlight = new Flight("FL456", "NYC", "SFO", LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(9), 50, "AVAILABLE");

        when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));
        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

        Flight result = flightService.updateFlight(1L, updatedFlight);

        assertEquals(updatedFlight.getFlightCode(), result.getFlightCode());
        assertEquals(updatedFlight.getDestination(), result.getDestination());
        verify(flightRepository, times(1)).findById(1L);
        verify(flightRepository, times(1)).save(existingFlight);
    }

    @Test
    public void testUpdateFlight_FlightNotFound() {
        Flight updatedFlight = new Flight("FL456", "NYC", "SFO", LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(9), 50, "AVAILABLE");

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> flightService.updateFlight(1L, updatedFlight));

        assertEquals("Flight not found with ID: 1", exception.getMessage());
        verify(flightRepository, times(1)).findById(1L);
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    public void testDeleteFlight_Successful() {
        when(flightRepository.existsById(1L)).thenReturn(true);

        flightService.deleteFlight(1L);

        verify(flightRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteFlight_FlightNotFound() {
        when(flightRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> flightService.deleteFlight(1L));

        assertEquals("Flight not found with ID: 1", exception.getMessage());
        verify(flightRepository, never()).deleteById(1L);
    }

    @Test
    public void testSearchFlights() {
        List<Flight> flights = List.of(new Flight("FL123", "NYC", "LAX", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(4), 50, "AVAILABLE"));

        when(flightRepository.findBySourceAndDestination("NYC", "LAX")).thenReturn(flights);

        List<Flight> result = flightService.searchFlights("NYC", "LAX");

        assertEquals(flights, result);
        verify(flightRepository, times(1)).findBySourceAndDestination("NYC", "LAX");
    }

}