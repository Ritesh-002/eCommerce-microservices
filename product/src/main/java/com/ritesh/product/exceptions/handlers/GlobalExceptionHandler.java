package com.ritesh.product.exceptions.handlers;

import com.ritesh.product.exceptions.InsufficientStockException;
import com.ritesh.product.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.parsing.Problem;
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

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Product not found!");
        problemDetail.setProperty("code", "PRODUCT_NOT_FOUND");

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

    @ExceptionHandler(InstantiationException.class)
    public ProblemDetail handleInsufficientStockException(InsufficientStockException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problemDetail.setTitle("Desired amount of quantity is not available, try to decrease the product quantity");
        problemDetail.setProperty("code", "STOCK_NOT_AVAILABLE");

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
