package airlinereservation.project.Airlinereservation.services;

import org.springframework.stereotype.Service;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.models.Reservation;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;
import airlinereservation.project.Airlinereservation.repositories.ReservationRepository;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;

    public ReservationService(ReservationRepository reservationRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));
    }

    public Reservation createReservation(Long flightId, Long userId, int reservedSeats) {
        return null;
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

        reservationRepository.delete(reservation);
    }
}
