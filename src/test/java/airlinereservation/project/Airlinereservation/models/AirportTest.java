package airlinereservation.project.Airlinereservation.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AirportTest {

    @Test
    void testAirportConstructorAndGetters() {
        Airport airport = new Airport("John F. Kennedy International Airport", "JFK", "New York", "USA");

        assertEquals("John F. Kennedy International Airport", airport.getName());
        assertEquals("JFK", airport.getCode());
        assertEquals("New York", airport.getCity());
        assertEquals("USA", airport.getCountry());
    }

    @Test
    void testSetters() {
        Airport airport = new Airport();
        airport.setId(1L);
        airport.setName("Los Angeles International Airport");
        airport.setCode("LAX");
        airport.setCity("Los Angeles");
        airport.setCountry("USA");

        assertEquals(1L, airport.getId());
        assertEquals("Los Angeles International Airport", airport.getName());
        assertEquals("LAX", airport.getCode());
        assertEquals("Los Angeles", airport.getCity());
        assertEquals("USA", airport.getCountry());
    }

    @Test
    void testToString() {
        Airport airport = new Airport("John F. Kennedy International Airport", "JFK", "New York", "USA");

        String expected = "Airport{id=null, name='John F. Kennedy International Airport', code='JFK', city='New York', country='USA'}";
        assertEquals(expected, airport.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Airport airport1 = new Airport("John F. Kennedy International Airport", "JFK", "New York", "USA");
        Airport airport2 = new Airport("John F. Kennedy International Airport", "JFK", "New York", "USA");
        Airport airport3 = new Airport("Los Angeles International Airport", "LAX", "Los Angeles", "USA");

        assertEquals(airport1, airport2);
        assertNotEquals(airport1, airport3);
        assertEquals(airport1.hashCode(), airport2.hashCode());
        assertNotEquals(airport1.hashCode(), airport3.hashCode());
    }
}