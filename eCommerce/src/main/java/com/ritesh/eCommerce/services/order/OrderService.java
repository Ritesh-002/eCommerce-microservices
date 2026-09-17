package com.ritesh.eCommerce.services.order;

import com.ritesh.eCommerce.enums.OrderStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void placeOrder(Long userId) {
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
            OrderItem orderItem = new OrderItem();
            orderItem.setQty(item.getQuantity());
            orderItem.setSubTotal(item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItem.setProduct(item.getProduct());

            orderItem.setOrder(order);


            product.setQty(product.getQty() - orderedQty);

            return orderItem;
        }).toList();

        order.setTotalAmount(orderItems.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPlacedOn(LocalDateTime.now());
        order.setUser(user);
        order.setOrderItems(orderItems);

        orderRepository.save(order);
        cartRepository.delete(cart);
    }

    public List<Order> getOrders(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

}
