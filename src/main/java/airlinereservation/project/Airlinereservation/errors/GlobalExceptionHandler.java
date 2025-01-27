package airlinereservation.project.Airlinereservation.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

   
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<InvalidRequestExceptionResponse> handleNotFoundException(NotFoundException ex) {
        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(
                ex.getStatus(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
    }

   
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<InvalidRequestExceptionResponse> handleInvalidRequestException(InvalidRequestException ex) {
        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(
                ex.getStatus(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
    }

   
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

   
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<InvalidRequestExceptionResponse> handleGenericException(Exception ex) {
        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}