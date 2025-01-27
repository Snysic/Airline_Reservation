package airlinereservation.project.Airlinereservation.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {

    @Test
    public void testDefaultConstructor() {
        Flight flight = new Flight();

        assertNull(flight.getId());
        assertNull(flight.getFlightCode());
        assertNull(flight.getSource());
        assertNull(flight.getDestination());
        assertNull(flight.getDepartureTime());
        assertNull(flight.getArrivalTime());
        assertEquals(0, flight.getAvailableSeats());
        assertNull(flight.getStatus());
        assertTrue(flight.isAvailable());
    }

    @Test
    public void testParameterizedConstructor() {
        String flightCode = "FL123";
        String source = "New York";
        String destination = "Los Angeles";
        LocalDateTime departureTime = LocalDateTime.of(2025, 1, 25, 14, 30);
        LocalDateTime arrivalTime = LocalDateTime.of(2025, 1, 25, 17, 30);
        int availableSeats = 100;
        String status = "Scheduled";

        Flight flight = new Flight(flightCode, source, destination, departureTime, arrivalTime, availableSeats, status);

        assertNull(flight.getId());
        assertEquals(flightCode, flight.getFlightCode());
        assertEquals(source, flight.getSource());
        assertEquals(destination, flight.getDestination());
        assertEquals(departureTime, flight.getDepartureTime());
        assertEquals(arrivalTime, flight.getArrivalTime());
        assertEquals(availableSeats, flight.getAvailableSeats());
        assertEquals(status, flight.getStatus());
        assertTrue(flight.isAvailable());
    }

    @Test
    public void testSettersAndGetters() {
        Flight flight = new Flight();

        Long id = 1L;
        String flightCode = "FL124";
        String source = "London";
        String destination = "Paris";
        LocalDateTime departureTime = LocalDateTime.of(2025, 2, 1, 10, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2025, 2, 1, 12, 0);
        int availableSeats = 150;
        String status = "On Time";
        boolean available = false;

        flight.setId(id);
        flight.setFlightCode(flightCode);
        flight.setSource(source);
        flight.setDestination(destination);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setAvailableSeats(availableSeats);
        flight.setStatus(status);
        flight.setAvailable(available);

        assertEquals(id, flight.getId());
        assertEquals(flightCode, flight.getFlightCode());
        assertEquals(source, flight.getSource());
        assertEquals(destination, flight.getDestination());
        assertEquals(departureTime, flight.getDepartureTime());
        assertEquals(arrivalTime, flight.getArrivalTime());
        assertEquals(availableSeats, flight.getAvailableSeats());
        assertEquals(status, flight.getStatus());
        assertFalse(flight.isAvailable());
    }

    @Test
    public void testToString() {
        String flightCode = "FL123";
        String source = "New York";
        String destination = "Los Angeles";
        LocalDateTime departureTime = LocalDateTime.of(2025, 1, 25, 14, 30);
        LocalDateTime arrivalTime = LocalDateTime.of(2025, 1, 25, 17, 30);
        int availableSeats = 100;
        String status = "Scheduled";

        Flight flight = new Flight(flightCode, source, destination, departureTime, arrivalTime, availableSeats, status);
        String expectedToString = "Flight{" +
                "id=null, flightCode='" + flightCode + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", availableSeats=" + availableSeats +
                ", status='" + status + '\'' +
                ", available=true" +
                '}';

        assertEquals(expectedToString, flight.toString());
    }
}
