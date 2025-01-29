package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.models.Flight;
import airlinereservation.project.Airlinereservation.services.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
@MockBean
    private FlightService flightService;

    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    void setUp() {
        flight1 = new Flight("FL123", "NYC", "LAX",
                LocalDateTime.of(2025, 1, 17, 8, 0),
                LocalDateTime.of(2025, 1, 17, 11, 0), 100, "AVAILABLE");

        flight2 = new Flight("FL456", "SFO", "ORD",
                LocalDateTime.of(2025, 1, 18, 14, 0),
                LocalDateTime.of(2025, 1, 18, 18, 0), 150, "AVAILABLE");
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetAllFlights() throws Exception {
        when(flightService.getAllFlights()).thenReturn(List.of(flight1, flight2));

        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].flightCode").value("FL123"))
                .andExpect(jsonPath("$[1].flightCode").value("FL456"));

        verify(flightService, times(1)).getAllFlights();
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetFlightById_Success() throws Exception {
        when(flightService.getFlightById(1L)).thenReturn(flight1);

        mockMvc.perform(get("/api/v1/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightCode").value("FL123"))
                .andExpect(jsonPath("$.source").value("NYC"))
                .andExpect(jsonPath("$.destination").value("LAX"));

        verify(flightService, times(1)).getFlightById(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateFlight_Success() throws Exception {
        when(flightService.createFlight(Mockito.any(Flight.class))).thenReturn(flight1);

        mockMvc.perform(post("/api/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightCode": "FL123",
                                  "source": "NYC",
                                  "destination": "LAX",
                                  "departureTime": "2025-01-17T08:00:00",
                                  "arrivalTime": "2025-01-17T11:00:00",
                                  "availableSeats": 100,
                                  "status": "AVAILABLE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightCode").value("FL123"));

        verify(flightService, times(1)).createFlight(Mockito.any(Flight.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateFlight_Success() throws Exception {
        when(flightService.updateFlight(eq(1L), Mockito.any(Flight.class))).thenReturn(flight1);

        mockMvc.perform(put("/api/v1/flights/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "flightCode": "FL123",
                                  "source": "NYC",
                                  "destination": "LAX",
                                  "departureTime": "2025-01-17T08:00:00",
                                  "arrivalTime": "2025-01-17T11:00:00",
                                  "availableSeats": 100,
                                  "status": "AVAILABLE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightCode").value("FL123"));

        verify(flightService, times(1)).updateFlight(eq(1L), Mockito.any(Flight.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteFlight_Success() throws Exception {
        doNothing().when(flightService).deleteFlight(1L);

        mockMvc.perform(delete("/api/v1/flights/1"))
                .andExpect(status().isNoContent());

        verify(flightService, times(1)).deleteFlight(1L);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testSearchFlights() throws Exception {
        when(flightService.searchFlights("NYC", "LAX", LocalDateTime.of(2025, 1, 17, 8, 0), 2))
                .thenReturn(List.of(flight1));

        mockMvc.perform(get("/api/v1/flights/search")
                        .param("source", "NYC")
                        .param("destination", "LAX")
                        .param("departureTime", "2025-01-17T08:00:00")
                        .param("seats", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].flightCode").value("FL123"));

        verify(flightService, times(1)).searchFlights("NYC", "LAX", LocalDateTime.of(2025, 1, 17, 8, 0), 2);
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isUnauthorized());
    }
}