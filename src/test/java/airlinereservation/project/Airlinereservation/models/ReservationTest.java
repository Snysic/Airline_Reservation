package airlinereservation.project.Airlinereservation.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    @Test
    public void testDefaultConstructor() {
        Reservation reservation = new Reservation();

        assertNull(reservation.getId());
        assertNull(reservation.getFlight());
        assertNull(reservation.getUser());
        assertNull(reservation.getReservationTime());
        assertEquals(0, reservation.getReservedSeats());
        assertNull(reservation.getStatus());
    }

    @Test
    public void testParameterizedConstructor() {
        Flight flight = new Flight();
        User user = new User();
        LocalDateTime reservationTime = LocalDateTime.of(2025, 1, 25, 10, 30);
        int reservedSeats = 2;
        String status = "Confirmed";

        Reservation reservation = new Reservation(flight, user, reservationTime, reservedSeats, status);

        assertNull(reservation.getId());
        assertEquals(flight, reservation.getFlight());
        assertEquals(user, reservation.getUser());
        assertEquals(reservationTime, reservation.getReservationTime());
        assertEquals(reservedSeats, reservation.getReservedSeats());
        assertEquals(status, reservation.getStatus());
    }

    @Test
    public void testSettersAndGetters() {
        Reservation reservation = new Reservation();

        Flight flight = new Flight();
        User user = new User();
        LocalDateTime reservationTime = LocalDateTime.of(2025, 1, 26, 12, 0);
        int reservedSeats = 5;
        String status = "Pending";

        reservation.setId(1L);
        reservation.setFlight(flight);
        reservation.setUser(user);
        reservation.setReservationTime(reservationTime);
        reservation.setReservedSeats(reservedSeats);
        reservation.setStatus(status);

        assertEquals(1L, reservation.getId());
        assertEquals(flight, reservation.getFlight());
        assertEquals(user, reservation.getUser());
        assertEquals(reservationTime, reservation.getReservationTime());
        assertEquals(reservedSeats, reservation.getReservedSeats());
        assertEquals(status, reservation.getStatus());
    }

    @Test
    public void testToString() {
        Flight flight = new Flight();
        User user = new User();
        LocalDateTime reservationTime = LocalDateTime.of(2025, 1, 26, 12, 0);
        int reservedSeats = 3;
        String status = "Cancelled";

        Reservation reservation = new Reservation(flight, user, reservationTime, reservedSeats, status);
        reservation.setId(1L);

        String expectedToString = "Reservation{" +
                "id=1, flight=" + flight +
                ", user=" + user +
                ", reservationTime=" + reservationTime +
                ", reservedSeats=" + reservedSeats +
                ", status='" + status + '\'' +
                '}';

        assertEquals(expectedToString, reservation.toString());
    }
}
