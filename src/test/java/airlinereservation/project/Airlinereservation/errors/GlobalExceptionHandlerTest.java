package airlinereservation.project.Airlinereservation.errors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException ex = new NotFoundException(404, "Resource not found");
        ResponseEntity<InvalidRequestExceptionResponse> response = globalExceptionHandler.handleNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void testHandleInvalidRequestException() {
        InvalidRequestException ex = new InvalidRequestException(400, "Invalid request data");
        ResponseEntity<InvalidRequestExceptionResponse> response = globalExceptionHandler.handleInvalidRequestException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Invalid request data", response.getBody().getMessage());
    }

    @Test
    void testHandleValidationExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("user", "username", "Username is required"),
                new FieldError("user", "email", "Email must be valid")
        ));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Username is required", response.getBody().get("username"));
        assertEquals("Email must be valid", response.getBody().get("email"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument provided");
        ResponseEntity<String> response = globalExceptionHandler.handleBadRequest(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument provided", response.getBody());
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Unexpected error occurred");
        ResponseEntity<InvalidRequestExceptionResponse> response = globalExceptionHandler.handleGenericException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Unexpected error occurred"));
    }
}
