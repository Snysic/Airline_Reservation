package airlinereservation.project.Airlinereservation.repositories;

import airlinereservation.project.Airlinereservation.models.Airport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AirportRepositoryTest {

    @Autowired
    private AirportRepository airportRepository;

    private Airport testAirport1;
    private Airport testAirport2;

    @BeforeEach
    void setUp() {
        testAirport1 = new Airport("John F. Kennedy International Airport", "JFK", "New York", "USA");
        testAirport2 = new Airport("Los Angeles International Airport", "LAX", "Los Angeles", "USA");
    }

    @Test
    void testSaveAndFindById() {
        Airport savedAirport = airportRepository.save(testAirport1);

        Optional<Airport> foundAirport = airportRepository.findById(savedAirport.getId());

        assertThat(foundAirport).isPresent();
        assertThat(foundAirport.get().getName()).isEqualTo("John F. Kennedy International Airport");
        assertThat(foundAirport.get().getCode()).isEqualTo("JFK");
    }

    @Test
    void testFindAll() {
        airportRepository.saveAll(List.of(testAirport1, testAirport2));

        List<Airport> airports = airportRepository.findAll();

        assertThat(airports).hasSize(2);
        assertThat(airports.get(0).getCode()).isEqualTo("JFK");
        assertThat(airports.get(1).getCode()).isEqualTo("LAX");
    }

    @Test
    void testDeleteById() {
        Airport savedAirport = airportRepository.save(testAirport1);

        airportRepository.deleteById(savedAirport.getId());

        Optional<Airport> deletedAirport = airportRepository.findById(savedAirport.getId());
        assertThat(deletedAirport).isEmpty();
    }
}