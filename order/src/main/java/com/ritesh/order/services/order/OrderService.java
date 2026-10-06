package com.ritesh.order.services.order;

import com.ritesh.order.clients.CartClient;
import com.ritesh.order.clients.ProductClient;
import com.ritesh.order.clients.UserClient;
import com.ritesh.order.dto.cart.CartResponseDTO;
import com.ritesh.order.dto.order.OrderMapper;
import com.ritesh.order.dto.order.OrderResponseDTO;
import com.ritesh.order.dto.product.ProductResponseDTO;
import com.ritesh.order.dto.user.UserResponseDTO;
import com.ritesh.order.enums.OrderStatus;
import com.ritesh.order.exceptions.CartNotFoundException;
import com.ritesh.order.exceptions.EmptyCartException;
import com.ritesh.order.exceptions.UserNotFoundException;
import com.ritesh.order.models.order.Order;
import com.ritesh.order.models.order.OrderItem;
import com.ritesh.order.repository.order.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final UserClient userClient;
    private final ProductClient productClient;
    private final CartClient cartClient;

    @Transactional
    public OrderResponseDTO placeOrder(Long userId) {

        UserResponseDTO user = userClient.getUser(userId);

        if (user == null) {
            throw new UserNotFoundException(
                    "User with id " + userId + " not found"
            );
        }

        CartResponseDTO cart = cartClient.getCart(userId);

        if (cart == null) {
            throw new CartNotFoundException(
                    "Cart not found for user with id " + userId
            );
        }

        if (cart.getCartItems() == null ||
                cart.getCartItems().isEmpty()) {

            throw new EmptyCartException(
                    "Cannot place order because the cart is empty"
            );
        }

        Order order = new Order();
        order.setUserId(userId);

        List<OrderItem> orderItems = cart.getCartItems()
                .stream()
                .map(item -> {

                    productClient.reserveProductQuantity(
                            item.getProductId(),
                            item.getQty()
                    );

                    ProductResponseDTO product =
                            productClient.getProduct(
                                    item.getProductId()
                            );

                    return orderMapper.toOrderItemEntity(
                            order,
                            item,
                            product
                    );
                })
                .toList();

        order.setOrderItems(orderItems);

        order.setTotalAmount(
                orderItems.stream()
                        .map(OrderItem::getSubTotal)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        )
        );

        order.setStatus(OrderStatus.CONFIRMED);
        order.setPlacedOn(LocalDateTime.now());

        Order savedOrder =
                orderRepository.save(order);

        cartClient.clearCart(userId);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    public List<OrderResponseDTO> getOrders(Long userId) {

        UserResponseDTO user = userClient.getUser(userId);

        if (user == null) {
            throw new UserNotFoundException(
                    "User with id " + userId + " not found"
            );
        }

        return orderRepository.findOrdersByUserId(userId)
                .stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();
    }
}
