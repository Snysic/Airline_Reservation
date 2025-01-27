package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.services.FlightService;
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

class FlightControllerTest {

    @InjectMocks
    private FlightController flightController;

    @Mock
    private FlightService flightService;

    private Flight flight;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flight = new Flight(
                "FL123",
                "New York",
                "Los Angeles",
                LocalDateTime.of(2025, 1, 17, 8, 0),
                LocalDateTime.of(2025, 1, 17, 11, 0),
                100,
                "AVAILABLE"
        );
        flight.setId(1L);
    }

    @Test
    void testGetAllFlights() {
        List<Flight> flights = Arrays.asList(flight);
        when(flightService.getAllFlights()).thenReturn(flights);

        ResponseEntity<List<Flight>> response = flightController.getAllFlights();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(flights);
        verify(flightService, times(1)).getAllFlights();
    }

    @Test
    void testGetFlightById() {
        when(flightService.getFlightById(1L)).thenReturn(flight);

        ResponseEntity<Flight> response = flightController.getFlightById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(flight);
        verify(flightService, times(1)).getFlightById(1L);
    }

    @Test
    void testCreateFlight() {
        when(flightService.createFlight(flight)).thenReturn(flight);

        ResponseEntity<Flight> response = flightController.createFlight(flight);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(flight);
        verify(flightService, times(1)).createFlight(flight);
    }

    @Test
    void testUpdateFlight() {
        Flight updatedFlight = new Flight(
                "FL123",
                "New York",
                "San Francisco",
                LocalDateTime.of(2025, 1, 18, 9, 0),
                LocalDateTime.of(2025, 1, 18, 12, 0),
                90,
                "AVAILABLE"
        );
        updatedFlight.setId(1L);
        when(flightService.updateFlight(1L, updatedFlight)).thenReturn(updatedFlight);

        ResponseEntity<Flight> response = flightController.updateFlight(1L, updatedFlight);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedFlight);
        verify(flightService, times(1)).updateFlight(1L, updatedFlight);
    }

    @Test
    void testDeleteFlight() {
        ResponseEntity<Void> response = flightController.deleteFlight(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(flightService, times(1)).deleteFlight(1L);
    }
}