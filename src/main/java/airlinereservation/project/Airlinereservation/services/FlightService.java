package airlinereservation.project.Airlinereservation.services;

import org.springframework.stereotype.Service;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.repositories.FlightRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + id));
    }

    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + id));

        existingFlight.setFlightCode(updatedFlight.getFlightCode());
        existingFlight.setSource(updatedFlight.getSource());
        existingFlight.setDestination(updatedFlight.getDestination());
        existingFlight.setDepartureTime(updatedFlight.getDepartureTime());
        existingFlight.setArrivalTime(updatedFlight.getArrivalTime());
        existingFlight.setAvailableSeats(updatedFlight.getAvailableSeats());
        existingFlight.setStatus(updatedFlight.getStatus());

        return flightRepository.save(existingFlight);
    }

    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found with ID: " + id);
        }
        flightRepository.deleteById(id);
    }

    public List<Flight> searchFlights(String source, String destination) {
        return flightRepository.findBySourceAndDestination(source, destination);
    }

    public List<Flight> getActiveFlights() {
        return flightRepository.findByStatus("ACTIVE");
    }

    public List<Flight> getFlightsAfter(LocalDateTime departureTime) {
        return flightRepository.findByDepartureTimeAfter(departureTime);
    }
}
