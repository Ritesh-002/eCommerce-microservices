package com.ritesh.order.controllers.order;

import com.ritesh.order.dto.order.OrderMapper;
import com.ritesh.order.dto.order.OrderResponseDTO;
import com.ritesh.order.models.order.Order;
import com.ritesh.order.services.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> fetchAllOrders(@PathVariable Long userId) {
        List<Order> allOrders = orderService.getOrders(userId);
        List<OrderResponseDTO> allOrderResponseDTOS = new ArrayList<>();
        if(allOrders!=null) {
            for(Order order: allOrders) {
                allOrderResponseDTOS.add(orderMapper.toOrderResponseDTO(order));
            }
            return new ResponseEntity<>(allOrderResponseDTOS, HttpStatus.CREATED);
        }
        return  ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        if(order!=null) {
            return new ResponseEntity<>(orderMapper.toOrderResponseDTO(order), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

}
