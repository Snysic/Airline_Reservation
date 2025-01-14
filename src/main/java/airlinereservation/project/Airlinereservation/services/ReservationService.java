package airlinereservation.project.Airlinereservation.services;

import org.springframework.stereotype.Service;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, 
                              FlightRepository flightRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));
    }

    public Reservation createReservation(Long flightId, Long userId, int reservedSeats) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + flightId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (flight.getAvailableSeats() < reservedSeats) {
            throw new RuntimeException("Not enough available seats for flight ID: " + flightId);
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - reservedSeats);
        flightRepository.save(flight);

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setUser(user);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setReservedSeats(reservedSeats);
        reservation.setStatus("CONFIRMED");

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));


        existingReservation.setReservedSeats(updatedReservation.getReservedSeats());
        existingReservation.setStatus(updatedReservation.getStatus());
        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));

        Flight flight = reservation.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + reservation.getReservedSeats());
        flightRepository.save(flight);

        reservationRepository.deleteById(id);
    }

    public List<Reservation> getReservationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getReservationsForFlight(Long flightId) {
        return reservationRepository.findByFlightId(flightId);
    }
}
