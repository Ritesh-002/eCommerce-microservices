package com.ritesh.eCommerce.controllers.order;

import com.ritesh.eCommerce.dto.cart.CartResponseDTO;
import com.ritesh.eCommerce.models.order.Order;
import com.ritesh.eCommerce.services.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> fetchAllOrders(@PathVariable Long userId) {
        List<Order> allOrders = orderService.getOrders(userId);
        if(allOrders!=null) {
            return new ResponseEntity<>(allOrders, HttpStatus.OK);
        }
        return  ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        if(order!=null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

}
