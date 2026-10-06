package com.ritesh.order.controllers.order;

import com.ritesh.order.dto.order.OrderResponseDTO;
import com.ritesh.order.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> fetchAllOrders(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrders(userId)
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable Long userId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        orderService.placeOrder(userId)
                );
    }
}
