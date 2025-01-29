package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.Airport;
import airlinereservation.project.Airlinereservation.services.AirportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AirportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AirportService airportService;

    @InjectMocks
    private AirportController airportController;

    private Airport testAirport1;
    private Airport testAirport2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(airportController).build();

        testAirport1 = new Airport("John F. Kennedy International Airport", "JFK", "New York", "USA");
        testAirport2 = new Airport("Los Angeles International Airport", "LAX", "Los Angeles", "USA");
    }

    @Test
    void testGetAllAirports() throws Exception {
        when(airportService.getAllAirports()).thenReturn(List.of(testAirport1, testAirport2));

        mockMvc.perform(get("/api/v1/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].code").value("JFK"))
                .andExpect(jsonPath("$[1].code").value("LAX"));

        verify(airportService, times(1)).getAllAirports();
    }

    @Test
    void testGetAirportById() throws Exception {
        when(airportService.getAirportById(1L)).thenReturn(testAirport1);

        mockMvc.perform(get("/api/v1/airports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("JFK"))
                .andExpect(jsonPath("$.city").value("New York"));

        verify(airportService, times(1)).getAirportById(1L);
    }

    @Test
    void testCreateAirport() throws Exception {
        when(airportService.createAirport(any(Airport.class))).thenReturn(testAirport1);

        mockMvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "John F. Kennedy International Airport",
                                  "code": "JFK",
                                  "city": "New York",
                                  "country": "USA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John F. Kennedy International Airport"))
                .andExpect(jsonPath("$.code").value("JFK"));

        verify(airportService, times(1)).createAirport(any(Airport.class));
    }

    @Test
    void testUpdateAirport() throws Exception {
        when(airportService.updateAirport(eq(1L), any(Airport.class))).thenReturn(testAirport2);

        mockMvc.perform(put("/api/v1/airports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Los Angeles International Airport",
                                  "code": "LAX",
                                  "city": "Los Angeles",
                                  "country": "USA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("LAX"))
                .andExpect(jsonPath("$.city").value("Los Angeles"));

        verify(airportService, times(1)).updateAirport(eq(1L), any(Airport.class));
    }

    @Test
    void testDeleteAirport() throws Exception {
        doNothing().when(airportService).deleteAirport(1L);

        mockMvc.perform(delete("/api/v1/airports/1"))
                .andExpect(status().isNoContent());

        verify(airportService, times(1)).deleteAirport(1L);
    }
}
