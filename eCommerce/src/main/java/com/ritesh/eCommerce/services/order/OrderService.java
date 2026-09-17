package com.ritesh.eCommerce.services.order;

import com.ritesh.eCommerce.dto.order.OrderMapper;
import com.ritesh.eCommerce.models.cart.Cart;
import com.ritesh.eCommerce.models.cart.CartItem;
import com.ritesh.eCommerce.models.order.Order;
import com.ritesh.eCommerce.models.order.OrderItem;
import com.ritesh.eCommerce.models.products.Product;
import com.ritesh.eCommerce.models.users.User;
import com.ritesh.eCommerce.repository.cart.CartRepository;
import com.ritesh.eCommerce.repository.order.OrderRepository;
import com.ritesh.eCommerce.repository.users.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public Order placeOrder(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        List<CartItem> cartItems = cart.getCartItems();

        List<OrderItem> orderItems = cartItems.stream().map(item -> {

            Integer orderedQty = item.getQuantity();
            Product product = item.getProduct();

            if (product.getQty() < orderedQty) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            OrderItem orderItem = orderMapper.toOrderItemEntity(order, item);

            product.setQty(product.getQty() - orderedQty);

            return orderItem;

        }).toList();

        orderMapper.toOrderEntity(order, user, orderItems);

        orderRepository.save(order);
        cartRepository.delete(cart);

        return order;
    }

    public List<Order> getOrders(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

}
