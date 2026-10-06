package com.ritesh.cart.exceptions.handlers;

import com.ritesh.cart.exceptions.CartItemNotFoundException;
import com.ritesh.cart.exceptions.CartItemOwnershipException;
import com.ritesh.cart.exceptions.CartNotFoundException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {

    @ExceptionHandler(CartNotFoundException.class)
    public ProblemDetail handleProductNotFound(CartNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Cart not found!");
        problemDetail.setProperty("code", "CART_NOT_FOUND");

        return problemDetail;
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ProblemDetail handleProductNotFound(CartItemNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Cart item not found!");
        problemDetail.setProperty("code", "CART_ITEM_NOT_FOUND");

        return problemDetail;
    }

    @ExceptionHandler(CartItemOwnershipException.class)
    public ProblemDetail handleProductNotFound(CartItemOwnershipException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Cart does not belong to the user!");
        problemDetail.setProperty("code", "CART_NOT_ASSOCIATED");

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "The provided SKU already exists" // We should not return internal exception messages to the user, therefore written manually they are used for internal purposes
        );

        problemDetail.setTitle("Duplicate SKU");
        problemDetail.setProperty("code", "SKU_ALREADY_EXISTS");

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
