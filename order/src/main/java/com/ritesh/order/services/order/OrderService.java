package com.ritesh.order.services.order;

import com.ritesh.order.clients.CartClient;
import com.ritesh.order.clients.ProductClient;
import com.ritesh.order.clients.UserClient;
import com.ritesh.order.dto.cart.CartResponseDTO;
import com.ritesh.order.dto.order.OrderMapper;
import com.ritesh.order.dto.order.OrderResponseDTO;
import com.ritesh.order.dto.product.ProductResponseDTO;
import com.ritesh.order.dto.user.UserResponseDTO;
import com.ritesh.order.enums.OrderEvents;
import com.ritesh.order.enums.OrderStatus;
import com.ritesh.order.events.OrderCreatedEvent;
import com.ritesh.order.exceptions.AccessDeniedException;
import com.ritesh.order.exceptions.CartNotFoundException;
import com.ritesh.order.exceptions.EmptyCartException;
import com.ritesh.order.exceptions.UserNotFoundException;
import com.ritesh.order.models.eventModels.OrderOutboxEvent;
import com.ritesh.order.models.order.Order;
import com.ritesh.order.models.order.OrderItem;
import com.ritesh.order.repository.order.OrderRepository;
import com.ritesh.order.repository.order.eventRepositories.OrderOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

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

    private final OrderOutboxEventRepository orderOutboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OrderResponseDTO placeOrder(Long userId, Jwt jwt) {

        UserResponseDTO user = userClient.getUser(userId, jwt);

        if (user == null) {
            throw new UserNotFoundException(
                    "User with id " + userId + " not found"
            );
        }

        // Get the cart owner's Keycloak ID from User Service.
        String cartOwnerKeycloakId =
                userClient.getUserKeycloakId(userId, jwt);

        // Ensure the authenticated customer owns this order.
        if (!jwt.getSubject().equals(cartOwnerKeycloakId)) {
            throw new AccessDeniedException(
                    "You are not allowed to place this order"
            );
        }

        CartResponseDTO cart = cartClient.getCart(userId, jwt);

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
                            item.getQty(),
                            jwt
                    );

                    ProductResponseDTO product =
                            productClient.getProduct(
                                    item.getProductId(),
                                    jwt
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

        // 1. Save the order first
        Order savedOrder = orderRepository.save(order);

// 2. Create the outbox event without its final payload
        OrderOutboxEvent outboxEvent = new OrderOutboxEvent(
                savedOrder.getId(),
                OrderEvents.OrderCreated,
                "{}"
        );

// 3. Save it to generate the outbox ID
        outboxEvent = orderOutboxEventRepository.saveAndFlush(outboxEvent);

// 4. Use the generated outbox ID as the event ID
        OrderCreatedEvent event = new OrderCreatedEvent(
                outboxEvent.getId(),
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getTotalAmount(),
                savedOrder.getPlacedOn()
        );

// 5. Serialize the complete event
        String payload = objectMapper.writeValueAsString(event);

// 6. Update the outbox row with the complete payload
        outboxEvent.setPayload(payload);
        orderOutboxEventRepository.save(outboxEvent);

        cartClient.clearCart(userId, jwt);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    public List<OrderResponseDTO> getOrders(Long userId, Jwt jwt) {

        UserResponseDTO user = userClient.getUser(userId, jwt);

        if (user == null) {
            throw new UserNotFoundException(
                    "User with id " + userId + " not found"
            );
        }

        // Get the cart owner's Keycloak ID from User Service.
        String cartOwnerKeycloakId =
                userClient.getUserKeycloakId(userId, jwt);

        // Ensure the authenticated customer owns this cart.
        if (!jwt.getSubject().equals(cartOwnerKeycloakId)) {
            throw new AccessDeniedException(
                    "You are not allowed to access these orders!"
            );
        }

        return orderRepository.findOrdersByUserId(userId)
                .stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();
    }
}
