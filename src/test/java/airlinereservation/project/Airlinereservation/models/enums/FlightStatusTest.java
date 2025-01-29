package airlinereservation.project.Airlinereservation.models.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlightStatusTest {

    @Test
    public void testEnumValues() {
        FlightStatus[] statuses = FlightStatus.values();

        assertEquals(4, statuses.length);
        assertEquals(FlightStatus.SCHEDULED, statuses[0]);
        assertEquals(FlightStatus.ACTIVE, statuses[1]);
        assertEquals(FlightStatus.COMPLETED, statuses[2]);
        assertEquals(FlightStatus.CANCELLED, statuses[3]);
    }

    @Test
    public void testValueOf() {
        assertEquals(FlightStatus.SCHEDULED, FlightStatus.valueOf("SCHEDULED"));
        assertEquals(FlightStatus.ACTIVE, FlightStatus.valueOf("ACTIVE"));
        assertEquals(FlightStatus.COMPLETED, FlightStatus.valueOf("COMPLETED"));
        assertEquals(FlightStatus.CANCELLED, FlightStatus.valueOf("CANCELLED"));
    }

    @Test
    public void testEnumToString() {
        assertEquals("SCHEDULED", FlightStatus.SCHEDULED.toString());
        assertEquals("ACTIVE", FlightStatus.ACTIVE.toString());
        assertEquals("COMPLETED", FlightStatus.COMPLETED.toString());
        assertEquals("CANCELLED", FlightStatus.CANCELLED.toString());
    }
}
