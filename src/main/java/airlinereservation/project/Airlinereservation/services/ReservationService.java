package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.models.User;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;
import airlinereservation.project.Airlinereservation.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final ReservationLockService reservationLockService;

    public ReservationService(ReservationRepository reservationRepository, 
                              FlightRepository flightRepository, 
                              UserRepository userRepository,
                              ReservationLockService reservationLockService) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.reservationLockService = reservationLockService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "Reservation not found with ID: " + id));
    }

    @Transactional
    public Reservation createReservation(Long flightId, Long userId, int reservedSeats) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException(404, "Flight not found with ID: " + flightId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(404, "User not found with ID: " + userId));

        if (reservationLockService.isSeatLocked(flightId)) {
            throw new IllegalStateException("Seats for this flight are temporarily locked due to an ongoing reservation.");
        }

        if (flight.getAvailableSeats() < reservedSeats) {
            throw new IllegalArgumentException("Not enough seats available for flight: " + flight.getFlightCode());
        }

        reservationLockService.lockSeats(flightId);
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

    @Transactional
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

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "Reservation not found with ID: " + id));

        Flight flight = reservation.getFlight();

        flight.setAvailableSeats(flight.getAvailableSeats() + reservation.getReservedSeats());
        flightRepository.save(flight);

        reservationRepository.delete(reservation);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(404, "User not found with ID: " + userId));

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
