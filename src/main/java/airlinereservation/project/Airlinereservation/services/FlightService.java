package airlinereservation.project.Airlinereservation.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        logger.info("Fetching all flights from the database.");
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        logger.info("Fetching flight with ID: {}", id);
        return flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "Flight not found with ID: " + id));
    }

    public boolean existsById(Long id) {
        return flightRepository.existsById(id);
    }

    public Flight createFlight(Flight flight) {
        if (flight == null || flight.getFlightCode() == null) {
            logger.error("Invalid flight details: flight or flightCode is null.");
            throw new IllegalArgumentException("Invalid flight details");
        }

        if (flight.getDepartureTime().isBefore(LocalDateTime.now())) {
            logger.error("Flight creation failed: departure time is in the past.");
            throw new IllegalArgumentException("Cannot create a flight with a past departure time.");
        }

        if (flight.getAvailableSeats() <= 0) {
            logger.error("Flight creation failed: no available seats.");
            throw new IllegalArgumentException("Flight must have available seats greater than zero.");
        }

        flight.setStatus("AVAILABLE");
        logger.info("Creating new flight: {}", flight.getFlightCode());
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        logger.info("Updating flight with ID: {}", id);
        Flight existingFlight = getFlightById(id);

        if (updatedFlight.getFlightCode() == null) {
            throw new IllegalArgumentException("Flight code cannot be null");
        }

        existingFlight.setFlightCode(updatedFlight.getFlightCode());
        existingFlight.setSource(updatedFlight.getSource());
        existingFlight.setDestination(updatedFlight.getDestination());
        existingFlight.setDepartureTime(updatedFlight.getDepartureTime());
        existingFlight.setArrivalTime(updatedFlight.getArrivalTime());
        existingFlight.setAvailableSeats(updatedFlight.getAvailableSeats());

        if (updatedFlight.getAvailableSeats() <= 0 || updatedFlight.getDepartureTime().isBefore(LocalDateTime.now())) {
            existingFlight.setStatus("UNAVAILABLE");
        } else {
            existingFlight.setStatus("AVAILABLE");
        }

        logger.info("Flight with ID: {} updated successfully.", id);
        return flightRepository.save(existingFlight);
    }

    public void deleteFlight(Long id) {
        logger.info("Deleting flight with ID: {}", id);
        if (!existsById(id)) {
            logger.error("Flight deletion failed: flight not found with ID: {}", id);
            throw new NotFoundException(404, "Flight not found with ID: " + id);
        }
        flightRepository.deleteById(id);
    }

    public List<Flight> searchFlights(String source, String destination) {
        logger.info("Searching flights from '{}' to '{}'", source, destination);
        return flightRepository.findBySourceAndDestination(source, destination);
    }

    @Scheduled(fixedRate = 900000) 
    public void updateFlightStatuses() {
        logger.info("Running scheduled task to update flight statuses.");
        List<Flight> flights = flightRepository.findAll();

        flights.stream()
                .filter(flight -> flight.getAvailableSeats() <= 0 || flight.getDepartureTime().isBefore(LocalDateTime.now()))
                .forEach(flight -> {
                    flight.setStatus("UNAVAILABLE");
                    flightRepository.save(flight);
                    logger.info("Flight {} status updated to UNAVAILABLE.", flight.getFlightCode());
                });
    }
}

