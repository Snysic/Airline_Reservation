package airlinereservation.project.Airlinereservation.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "reserved_seats", nullable = false)
    private int reservedSeats;

    @Column(name = "status", nullable = false)
    private String status;

    public Reservation() {
    }

    public Reservation(Flight flight, User user, LocalDateTime reservationTime, int reservedSeats, String status) {
        this.flight = flight;
        this.user = user;
        this.reservationTime = reservationTime;
        this.reservedSeats = reservedSeats;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", flight=" + flight +
                ", user=" + user +
                ", reservationTime=" + reservationTime +
                ", reservedSeats=" + reservedSeats +
                ", status='" + status + '\'' +
                '}';
    }
}
