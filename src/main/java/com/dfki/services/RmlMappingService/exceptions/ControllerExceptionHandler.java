package com.dfki.services.RmlMappingService.exceptions;

import com.dfki.services.RmlMappingService.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<Object> handleAllExceptions(final Exception e) {
        List<String> details = new ArrayList<>();
        details.add(e.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse("Server Error", details);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(value = {ServiceConnectionException.class})
    public final ResponseEntity<Object> handleConnectionException(final ServiceConnectionException e) {
        List<String> details = new ArrayList<>();
        details.add(e.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse("Connection to the "
                + e.getServiceName() + " service failed.", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {RmlMappingException.class})
    public final ResponseEntity<Object> handleRmlMappingException(final RmlMappingException e) {
        List<String> details = new ArrayList<>();
        details.add(e.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse("Input structure is neither "
                + "a valid XML nor JSON.", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {InvalidInputException.class})
    public final ResponseEntity<Object> handleInvalidInputException(final InvalidInputException e) {
        List<String> details = new ArrayList<>();
        details.add(e.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getInputType() + " input is not valid.",
                details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
