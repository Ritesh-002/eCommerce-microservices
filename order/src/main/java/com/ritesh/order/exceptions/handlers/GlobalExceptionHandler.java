package com.ritesh.order.exceptions.handlers;

import com.ritesh.order.exceptions.CartNotFoundException;
import com.ritesh.order.exceptions.EmptyCartException;
import com.ritesh.order.exceptions.OrderNotFoundException;
import com.ritesh.order.exceptions.UserNotFoundException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;


public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleProductNotFound(OrderNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Order not found!");
        problemDetail.setProperty("code", "ORDER_NOT_FOUND");

        return problemDetail;
    }

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

    @ExceptionHandler(EmptyCartException.class)
    public ProblemDetail handleProductNotFound(EmptyCartException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Can't place order because cart is empty!");
        problemDetail.setProperty("code", "CART_IS_EMPTY");

        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleProductNotFound(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("User not found!");
        problemDetail.setProperty("code", "USER_NOT_FOUND");

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
