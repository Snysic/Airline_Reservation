package airlinereservation.project.Airlinereservation.services;


import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public AdminService(FlightRepository flightRepository,
                        ReservationRepository reservationRepository,
                        UserRepository userRepository) {
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public Flight createFlight(Flight flight) {
        if (flight == null || flight.getFlightCode() == null || flight.getFlightCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Flight data is invalid: Flight code is required");
        }

        if (flight.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("Flight must have at least one available seat");
        }

        return flightRepository.save(flight);
    }

    public void deleteFlight(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new IllegalArgumentException("Flight not found with ID: " + flightId);
        }

        flightRepository.deleteById(flightId);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        if (reservations.isEmpty()) {
            throw new IllegalStateException("No reservations found");
        }

        return reservations;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new IllegalStateException("No users found");
        }

        return users;
    }

    public void updateFlightStatus() {
        List<Flight> flights = flightRepository.findAll();

        for (Flight flight : flights) {
            if (flight.getAvailableSeats() <= 0 || flight.getDepartureTime().isBefore(LocalDateTime.now())) {
                flight.setAvailable(false);
                flightRepository.save(flight);
            }
        }
    }
}
