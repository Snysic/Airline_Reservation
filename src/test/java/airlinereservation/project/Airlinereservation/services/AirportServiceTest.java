package airlinereservation.project.Airlinereservation.services;

import airlinereservation.project.Airlinereservation.errors.NotFoundException;
import airlinereservation.project.Airlinereservation.models.Airport;
import airlinereservation.project.Airlinereservation.repositories.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

    private Airport testAirport1;
    private Airport testAirport2;

    @BeforeEach
    void setUp() {
        testAirport1 = new Airport("John F. Kennedy International Airport", "JFK", "New York", "USA");
        testAirport2 = new Airport("Los Angeles International Airport", "LAX", "Los Angeles", "USA");
    }

    @Test
    void testGetAllAirports() {
        when(airportRepository.findAll()).thenReturn(List.of(testAirport1, testAirport2));

        List<Airport> airports = airportService.getAllAirports();

        assertThat(airports).hasSize(2);
        assertThat(airports.get(0).getCode()).isEqualTo("JFK");
        assertThat(airports.get(1).getCode()).isEqualTo("LAX");

        verify(airportRepository, times(1)).findAll();
    }

    @Test
    void testGetAirportById_Success() {
        when(airportRepository.findById(1L)).thenReturn(Optional.of(testAirport1));

        Airport foundAirport = airportService.getAirportById(1L);

        assertThat(foundAirport).isEqualTo(testAirport1);
        verify(airportRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAirportById_NotFound() {
        when(airportRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> airportService.getAirportById(999L));

        assertThat(exception.getMessage()).isEqualTo("Airport not found with ID: 999");
        verify(airportRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateAirport() {
        when(airportRepository.save(any(Airport.class))).thenReturn(testAirport1);

        Airport createdAirport = airportService.createAirport(testAirport1);

        assertThat(createdAirport).isEqualTo(testAirport1);
        verify(airportRepository, times(1)).save(testAirport1);
    }

    @Test
    void testUpdateAirport_Success() {
        when(airportRepository.findById(1L)).thenReturn(Optional.of(testAirport1));
        when(airportRepository.save(any(Airport.class))).thenReturn(testAirport2);

        Airport updatedAirport = airportService.updateAirport(1L, testAirport2);

        assertThat(updatedAirport.getName()).isEqualTo("Los Angeles International Airport");
        assertThat(updatedAirport.getCode()).isEqualTo("LAX");

        verify(airportRepository, times(1)).findById(1L);
        verify(airportRepository, times(1)).save(testAirport1);
    }

    @Test
    void testUpdateAirport_NotFound() {
        when(airportRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> airportService.updateAirport(999L, testAirport2));

        assertThat(exception.getMessage()).isEqualTo("Airport not found with ID: 999");
        verify(airportRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteAirport_Success() {
        when(airportRepository.findById(1L)).thenReturn(Optional.of(testAirport1));
        doNothing().when(airportRepository).delete(testAirport1);

        airportService.deleteAirport(1L);

        verify(airportRepository, times(1)).findById(1L);
        verify(airportRepository, times(1)).delete(testAirport1);
    }

    @Test
    void testDeleteAirport_NotFound() {
        when(airportRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> airportService.deleteAirport(999L));

        assertThat(exception.getMessage()).isEqualTo("Airport not found with ID: 999");
        verify(airportRepository, times(1)).findById(999L);
    }
}
