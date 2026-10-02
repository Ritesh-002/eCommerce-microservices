package com.ritesh.order.services.order;

import com.ritesh.order.clients.CartClient;
import com.ritesh.order.clients.ProductClient;
import com.ritesh.order.clients.UserClient;
//import com.ritesh.order.dto.cart.CartItemResponseDTO;
import com.ritesh.order.dto.cart.CartResponseDTO;
import com.ritesh.order.dto.order.OrderMapper;
import com.ritesh.order.dto.product.ProductResponseDTO;
import com.ritesh.order.dto.user.UserResponseDTO;
import com.ritesh.order.enums.OrderStatus;
//import com.ritesh.order.models.cart.Cart;
//import com.ritesh.order.models.cart.CartItem;
import com.ritesh.order.models.order.Order;
import com.ritesh.order.models.order.OrderItem;
//import com.ritesh.order.models.products.Product;
//import com.ritesh.order.models.users.User;
//import com.ritesh.order.repository.cart.CartRepository;
import com.ritesh.order.repository.order.OrderRepository;
//import com.ritesh.order.repository.users.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

//    private final CartRepository cartRepository;
//    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final UserClient userClient;
    private final ProductClient productClient;
    private final CartClient cartClient;

//    @Transactional
//    public Order placeOrder(Long userId) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserResponseDTO user = userClient.getUser(userId);
//
//        Cart cart = cartRepository.findCartByUserId(userId);
//
//        CartResponseDTO cart = cartClient.getCart(userId);
//
//        if (cart == null || cart.getCartItems().isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }
//
//        Order order = new Order();
//        order.setUserId(userId);
//
//        List<CartItemResponseDTO> cartItems = cart.getCartItems();
//
//        List<OrderItem> orderItems = cartItems.stream().map(item -> {
//
//            Integer orderedQty = item.getQty();
//            Product product = item.getProduct();
//            ProductResponseDTO product = productClient.getProduct(item.getProductId());
//
//            if (product.getQty() < orderedQty) {
//                throw new RuntimeException(
//                        "Insufficient stock for product: " + product.getProductName()
//                );
//            }
//
//            ProductResponseDTO product =
//                    productClient.reserveProductQuantity(
//                            item.getProductId(),
//                            item.getQty()
//                    );
//
//            OrderItem orderItem = orderMapper.toOrderItemEntity(order, item);
//
//
//
//
//            product.setQty(product.getQty() - orderedQty);
//
//            return orderItem;
//
//        }).toList();
//
//        orderMapper.toOrderEntity(order, user, orderItems);
//
//        orderRepository.save(order);
//        cartRepository.delete(cart);
//
//        cartClient.clearCart(userId);
//
//        return order;
//    }

    @Transactional
    public Order placeOrder(Long userId) {

        UserResponseDTO user = userClient.getUser(userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        CartResponseDTO cart = cartClient.getCart(userId);

        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
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

                    ProductResponseDTO product = productClient.getProduct(item.getProductId());
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
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        order.setStatus(OrderStatus.CONFIRMED);
        order.setPlacedOn(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        cartClient.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getOrders(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

}
