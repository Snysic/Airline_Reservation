package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.models.Airport;
import airlinereservation.project.Airlinereservation.repositories.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    public Airport getAirportById(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "Airport not found with ID: " + id));
    }

    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public Airport updateAirport(Long id, Airport updatedAirport) {
        Airport existingAirport = getAirportById(id);

        existingAirport.setName(updatedAirport.getName());
        existingAirport.setCode(updatedAirport.getCode());
        existingAirport.setCity(updatedAirport.getCity());
        existingAirport.setCountry(updatedAirport.getCountry());

        return airportRepository.save(existingAirport);
    }

    public void deleteAirport(Long id) {
        Airport airport = getAirportById(id);
        airportRepository.delete(airport);
    }
}
