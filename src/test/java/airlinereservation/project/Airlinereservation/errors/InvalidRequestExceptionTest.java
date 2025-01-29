package airlinereservation.project.Airlinereservation.errors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InvalidRequestExceptionTest {

    @Test
    public void testDefaultConstructor() {
        InvalidRequestException exception = new InvalidRequestException();
        assertNull(exception.getMessage());
        assertEquals(0, exception.getStatus());
    }

    @Test
    public void testParameterizedConstructor() {
        int expectedStatus = 400;
        String expectedMessage = "Invalid request";

        InvalidRequestException exception = new InvalidRequestException(expectedStatus, expectedMessage);

        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testSettersAndGetters() {
        InvalidRequestException exception = new InvalidRequestException();

        int expectedStatus = 404;
        String expectedMessage = "Resource not found";

        exception.setStatus(expectedStatus);
        exception.setMessage(expectedMessage);

        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testToString() {
        int expectedStatus = 500;
        String expectedMessage = "Internal server error";

        InvalidRequestException exception = new InvalidRequestException(expectedStatus, expectedMessage);

        String expectedToString = "InvalidRequestException{status=" + expectedStatus +
                ", message='" + expectedMessage + "'}";

        assertEquals(expectedToString, exception.toString());
    }
}
