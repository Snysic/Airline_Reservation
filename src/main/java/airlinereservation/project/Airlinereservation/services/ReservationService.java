package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.errors.NotFoundException;
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
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, FlightRepository flightRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "Reservation not found with ID: " + id));
    }

    public Reservation createReservation(Long flightId, Long userId, int reservedSeats) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException(404, "Flight not found with ID: " + flightId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(404, "User not found with ID: " + userId));

        if (flight.getAvailableSeats() < reservedSeats) {
            throw new IllegalArgumentException("Not enough seats available for flight: " + flight.getFlightCode());
        }

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setUser(user);
        reservation.setReservedSeats(reservedSeats);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus("CONFIRMED");

        flight.setAvailableSeats(flight.getAvailableSeats() - reservedSeats);
        flightRepository.save(flight);

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "Reservation not found with ID: " + id));

        int reservedSeatsDifference = updatedReservation.getReservedSeats() - existingReservation.getReservedSeats();
        Flight flight = existingReservation.getFlight();

        if (reservedSeatsDifference > 0 && flight.getAvailableSeats() < reservedSeatsDifference) {
            throw new IllegalArgumentException("Not enough seats available for flight: " + flight.getFlightCode());
        }

        existingReservation.setReservedSeats(updatedReservation.getReservedSeats());
        existingReservation.setStatus(updatedReservation.getStatus());

        flight.setAvailableSeats(flight.getAvailableSeats() - reservedSeatsDifference);
        flightRepository.save(flight);

        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "Reservation not found with ID: " + id));

        Flight flight = reservation.getFlight();

        flight.setAvailableSeats(flight.getAvailableSeats() + reservation.getReservedSeats());
        flightRepository.save(flight);

        reservationRepository.delete(reservation);
    }

    public List<Reservation> getReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getReservationsByFlight(Long flightId) {
        return reservationRepository.findByFlightId(flightId);
    }

    public boolean isFlightAvailableForReservation(Long flightId, int seatsRequested) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException(404, "Flight not found with ID: " + flightId));

        return flight.getAvailableSeats() >= seatsRequested;
    }
}
