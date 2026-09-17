package com.ritesh.eCommerce.dto.order;

import com.ritesh.eCommerce.enums.OrderStatus;
import com.ritesh.eCommerce.models.cart.CartItem;
import com.ritesh.eCommerce.models.order.Order;
import com.ritesh.eCommerce.models.order.OrderItem;
import com.ritesh.eCommerce.models.users.User;
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

        orderItemResponseDTO.setProductName(orderItem.getProduct().getName());
        orderItemResponseDTO.setProductId(orderItem.getProduct().getId());

        return orderItemResponseDTO;

    }
    public OrderItem toOrderItemEntity(Order order, CartItem cartItem) {

        OrderItem orderItem = new OrderItem();

        orderItem.setProduct(cartItem.getProduct());
        orderItem.setPriceAtCheckout(cartItem.getProduct().getPrice());
        orderItem.setQty(cartItem.getQuantity());
        orderItem.setSubTotal(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

        orderItem.setOrder(order);

        return orderItem;

    }
    public void toOrderEntity(Order order, User user, List<OrderItem> orderItems) {
        order.setTotalAmount(orderItems.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPlacedOn(LocalDateTime.now());
        order.setUser(user);
        order.setOrderItems(orderItems);
    }
}
