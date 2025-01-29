package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.services.AdminService;
import airlinereservation.project.Airlinereservation.services.UserService;
import airlinereservation.project.Airlinereservation.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final ReservationService reservationService;

    public AdminController(AdminService adminService, UserService userService, ReservationService reservationService) {
        this.adminService = adminService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createFlight(flight));
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        adminService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(adminService.getAllReservations());
    }

    @GetMapping("/reservations/user/{userId}")
public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
    User user = userService.getUserById(userId); 
    return ResponseEntity.ok(reservationService.getReservationsByUser(user.getId()));
}

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/flights/status")
    public ResponseEntity<String> updateFlightStatus() {
        adminService.updateFlightStatus();
        return ResponseEntity.ok("Flight statuses updated successfully");
    }
}

