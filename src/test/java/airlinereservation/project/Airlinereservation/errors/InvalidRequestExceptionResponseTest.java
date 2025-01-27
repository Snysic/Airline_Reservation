package airlinereservation.project.Airlinereservation.errors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InvalidRequestExceptionResponseTest {

    @Test
    public void testDefaultConstructor() {
        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse();
        assertEquals(0, response.getStatus());
        assertNull(response.getMessage());
    }

    @Test
    public void testParameterizedConstructor() {
        int expectedStatus = 400;
        String expectedMessage = "Invalid request";

        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(expectedStatus, expectedMessage);

        assertEquals(expectedStatus, response.getStatus());
        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    public void testSettersAndGetters() {
        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse();

        int expectedStatus = 404;
        String expectedMessage = "Resource not found";

        response.setStatus(expectedStatus);
        response.setMessage(expectedMessage);

        assertEquals(expectedStatus, response.getStatus());
        assertEquals(expectedMessage, response.getMessage());
    }

    @Test
    public void testToString() {
        int expectedStatus = 500;
        String expectedMessage = "Internal server error";

        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(expectedStatus, expectedMessage);

        String expectedToString = "InvalidRequestExceptionResponse{status=" + expectedStatus +
                ", message='" + expectedMessage + "'}";

        assertEquals(expectedToString, response.toString());
    }
}
