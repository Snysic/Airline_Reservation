package airlinereservation.project.Airlinereservation.errors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NotFoundExceptionTest {

    @Test
    public void testDefaultConstructor() {
        NotFoundException exception = new NotFoundException();
        assertNull(exception.getMessage());
        assertEquals(0, exception.getStatus());
    }

    @Test
    public void testParameterizedConstructor() {
        int expectedStatus = 404;
        String expectedMessage = "Resource not found";

        NotFoundException exception = new NotFoundException(expectedStatus, expectedMessage);

        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testSettersAndGetters() {
        NotFoundException exception = new NotFoundException();

        int expectedStatus = 404;
        String expectedMessage = "User not found";

        exception.setStatus(expectedStatus);
        exception.setMessage(expectedMessage);

        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testToString() {
        int expectedStatus = 404;
        String expectedMessage = "Flight not found";

        NotFoundException exception = new NotFoundException(expectedStatus, expectedMessage);

        String expectedToString = "NotFoundException{status=" + expectedStatus +
                ", message='" + expectedMessage + "'}";

        assertEquals(expectedToString, exception.toString());
    }
}
