package airlinereservation.project.Airlinereservation.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationRequestTest {

    @Test
    void testReservationRequestSettersAndGetters() {
        ReservationRequest request = new ReservationRequest();

        request.setFlightId(1L);
        request.setUserId(2L);
        request.setReservedSeats(3);

        assertEquals(1L, request.getFlightId(), "Flight ID should be 1");
        assertEquals(2L, request.getUserId(), "User ID should be 2");
        assertEquals(3, request.getReservedSeats(), "Reserved seats should be 3");
    }

    @Test
    void testConstructorAndInitialization() {
        ReservationRequest request = new ReservationRequest();

        assertNull(request.getFlightId(), "Flight ID should be null initially");
        assertNull(request.getUserId(), "User ID should be null initially");
        assertEquals(0, request.getReservedSeats(), "Reserved seats should be 0 initially");
    }
}

