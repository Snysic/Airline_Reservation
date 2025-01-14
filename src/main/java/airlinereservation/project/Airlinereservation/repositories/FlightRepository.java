package airlinereservation.project.Airlinereservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airlinereservation.project.Airlinereservation.models.Flight;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByStatus(String status);

    List<Flight> findBySourceAndDestination(String source, String destination);

    List<Flight> findByDepartureTimeAfter(LocalDateTime departureTime);

    List<Flight> findByAvailableSeatsGreaterThan(int availableSeats);

    List<Flight> findBySourceAndDestinationAndAvailableSeatsGreaterThan(String source, String destination, int availableSeats);
}
