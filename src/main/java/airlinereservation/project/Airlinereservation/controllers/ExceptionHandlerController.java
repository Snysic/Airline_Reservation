package airlinereservation.project.Airlinereservation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import airlinereservation.project.Airlinereservation.errors.InvalidRequestException;
import airlinereservation.project.Airlinereservation.errors.InvalidRequestExceptionResponse;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<InvalidRequestExceptionResponse> handleInvalidRequestException(InvalidRequestException ex) {
        InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        String errorMessage = "Error: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}

