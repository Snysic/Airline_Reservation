package airlinereservation.project.Airlinereservation.controllers;

import airlinereservation.project.Airlinereservation.errors.InvalidRequestException;
import airlinereservation.project.Airlinereservation.errors.InvalidRequestExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionHandlerControllerTest {

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleInvalidRequestException() {
        InvalidRequestException exception = new InvalidRequestException(400, "Invalid request");

        ResponseEntity<InvalidRequestExceptionResponse> response = exceptionHandlerController.handleInvalidRequestException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Invalid request", response.getBody().getMessage());
    }

    @Test
    void testHandleGeneralException() {
        Exception exception = new Exception("An unexpected error occurred");

        ResponseEntity<String> response = exceptionHandlerController.handleGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error: An unexpected error occurred", response.getBody());
    }
}
