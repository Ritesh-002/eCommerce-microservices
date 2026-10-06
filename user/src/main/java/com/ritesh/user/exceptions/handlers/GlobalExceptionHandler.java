package com.ritesh.user.exceptions.handlers;

import com.ritesh.user.exceptions.AddressNotBelongsToUser;
import com.ritesh.user.exceptions.UserAddressNotFoundException;
import com.ritesh.user.exceptions.UserNotFoundException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Note: Exceptions for authentication and authorization is implemented after keycloak implementation, we don't need to create manual handlers for auth errors as it is managed separately while implementing keycloak auth

// Categories of exceptions: DB exceptions, Auth exceptions, Validation exceptions, Duplicate exception and so on based on business requirement.

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("User not found!");
        problemDetail.setProperty("code", "USER_NOT_FOUND");

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Request validation failed"
                );

        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("code", "VALIDATION_ERROR");

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(AddressNotBelongsToUser.class)
    public ProblemDetail handleAddressNotBelongsToUser(AddressNotBelongsToUser ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Address doesn't belongs to user!");
        problemDetail.setProperty("code", "ADDRESS_NOT_ASSOCIATED");

        return problemDetail;
    }

    @ExceptionHandler(UserAddressNotFoundException.class)
    public ProblemDetail handleUserAddressNotFound(UserAddressNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("User address not found!");
        problemDetail.setProperty("code", "USER_ADDRESS_NOT_FOUND");

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "The email or phone you provided already exists" // We should not return internal exception messages to the user, therefore written manually they are used for internal purposes
        );

        problemDetail.setTitle("Duplicate phone/email");
        problemDetail.setProperty("code", "DATA_ALREADY_EXISTS");

        return problemDetail;
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ProblemDetail handleDataAccessResourceFailureException(
            DataAccessResourceFailureException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "The database is currently unavailable"
        );

        problemDetail.setTitle("Database Unavailable");
        problemDetail.setProperty("code", "DATABASE_UNAVAILABLE");

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {

//        log.error("Unexpected error occurred", ex);

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred"
                );

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty(
                "code",
                "INTERNAL_SERVER_ERROR"
        );

        return problemDetail;
    }

}
