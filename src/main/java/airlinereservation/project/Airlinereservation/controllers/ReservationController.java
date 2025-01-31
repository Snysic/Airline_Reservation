package airlinereservation.project.Airlinereservation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import airlinereservation.project.Airlinereservation.config.ReservationRequest;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.services.ReservationLockService;
import airlinereservation.project.Airlinereservation.services.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

     private final ReservationService reservationService;
    private final ReservationLockService reservationLockService;

    public ReservationController(ReservationService reservationService, ReservationLockService reservationLockService) {
        this.reservationService = reservationService;
        this.reservationLockService = reservationLockService;
    }

    @PostMapping("/lock")
    public ResponseEntity<String> lockSeats(@RequestBody ReservationRequest request) {
        if (reservationLockService.isSeatLocked(request.getFlightId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Seats are already locked for this flight.");
        }

        reservationLockService.lockSeats(request.getFlightId());
        return ResponseEntity.ok("Seats reserved for 15 minutes.");
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(
                reservation.getFlight().getId(),
                reservation.getUser().getId(),
                reservation.getReservedSeats()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        Reservation reservation = reservationService.updateReservation(id, updatedReservation);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}