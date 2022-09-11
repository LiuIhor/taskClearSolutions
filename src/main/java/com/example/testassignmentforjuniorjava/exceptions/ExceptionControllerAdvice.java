package com.example.testassignmentforjuniorjava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to catch possible exceptions
 */
@RestController
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final String ZONE_ID = "UTC";

    /**
     * Method to catch the exception that occurs when changing the fields of an entity
     *
     * @param exception MethodArgumentNotValidException
     * @return Information about exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DetailException handleValidationException(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new DetailException(errors.toString(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    /**
     * Method to catch subsequent custom exceptions
     *
     * @param exception CustomException
     * @return Information about exception
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DetailException handleCustomException(CustomException exception) {
        return new DetailException(exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    /**
     * Method to catch UserNotFoundException
     *
     * @param exception UserNotFoundException
     * @return Information about exception
     */
    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DetailException handleCustomException(UserNotFoundException exception) {
        return new DetailException(exception.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }
}