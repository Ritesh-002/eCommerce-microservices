package com.ritesh.order.dto.order;

import com.ritesh.order.dto.cart.CartItemResponseDTO;
import com.ritesh.order.dto.product.ProductResponseDTO;
import com.ritesh.order.enums.OrderStatus;
//import com.ritesh.order.models.cart.CartItem;
import com.ritesh.order.models.order.Order;
import com.ritesh.order.models.order.OrderItem;
//import com.ritesh.order.models.users.User;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    public OrderResponseDTO toOrderResponseDTO(Order order) {

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        List<OrderItemResponseDTO> orderItemResponseDTOS = new ArrayList<>();

        orderResponseDTO.setOrderId(order.getId());
        orderResponseDTO.setTotalAmount(order.getTotalAmount());
        orderResponseDTO.setStatus(order.getStatus());
        orderResponseDTO.setPlacedOn(order.getPlacedOn());

        List<OrderItem> allOrderItems = order.getOrderItems();

        for(OrderItem orderItem: allOrderItems) {
            orderItemResponseDTOS.add(toOrderItemResponseDTO(orderItem));
        }

        orderResponseDTO.setAllOrderItems(orderItemResponseDTOS);

        return orderResponseDTO;

    }
    public OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem) {
        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();

        orderItemResponseDTO.setOrderItemId(orderItem.getId());
        orderItemResponseDTO.setQty(orderItem.getQty());
        orderItemResponseDTO.setPricePerUnit(orderItem.getPriceAtCheckout());
        orderItemResponseDTO.setSubTotal(orderItem.getSubTotal());

        orderItemResponseDTO.setProductName(orderItem.getProductName());
        orderItemResponseDTO.setProductId(orderItem.getProductId());

        return orderItemResponseDTO;

    }
    public OrderItem toOrderItemEntity(
            Order order,
            CartItemResponseDTO cartItem,
            ProductResponseDTO product
    ) {

        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        System.out.println("Product ID: " + product.getId());
        System.out.println("Product ID from Cart item: " + cartItem.getProductId());
        System.out.println("Product name from product item: " + product.getName());
        orderItem.setProductId(cartItem.getProductId());
        orderItem.setProductName(product.getName());

        orderItem.setQty(cartItem.getQty());

        orderItem.setPriceAtCheckout(product.getPrice());

        orderItem.setSubTotal(
                product.getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQty()))
        );

        return orderItem;
    }
    public void toOrderEntity(Order order, Long userId, List<OrderItem> orderItems) {
        order.setTotalAmount(orderItems.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPlacedOn(LocalDateTime.now());
        order.setUserId(userId);
        order.setOrderItems(orderItems);
    }
}
