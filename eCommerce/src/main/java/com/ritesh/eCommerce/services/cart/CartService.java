package com.ritesh.eCommerce.services.cart;

import com.ritesh.eCommerce.dto.cart.CartRequestDTO;
import com.ritesh.eCommerce.models.cart.Cart;
import com.ritesh.eCommerce.models.cart.CartItem;
import com.ritesh.eCommerce.models.products.Product;
import com.ritesh.eCommerce.models.users.User;
import com.ritesh.eCommerce.repository.cart.CartItemRepository;
import com.ritesh.eCommerce.repository.cart.CartRepository;
import com.ritesh.eCommerce.repository.products.ProductRepository;
import com.ritesh.eCommerce.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Cart getCart(Long userId) {
        return cartRepository.findCartByUserId(userId);
    }

    public CartItem addToCart(Long userId, Long productId, Integer qty) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart existingCart = cartRepository.findCartByUserId(userId);

        CartItem cartItem;

        if (existingCart != null) {

            Optional<CartItem> existingItem = existingCart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {

                cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + qty);

            } else {

                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(qty);
                cartItem.setCart(existingCart);

                existingCart.getCartItems().add(cartItem);
            }

        } else {

            Cart cart = new Cart();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            cart.setUser(user);

            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(qty);
            cartItem.setCart(cart);

            cart.getCartItems().add(cartItem);

            cartRepository.save(cart);
        }

        return cartItem;
    }

    public boolean deleteCart(Long userId) {
        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart != null) {
            cartRepository.delete(cart);
            return true;
        }

        return false;
    }

    public boolean removeFromCart(Long userId, Long cartItemId) {
        Cart existingCart = cartRepository.findCartByUserId(userId);
        if(existingCart != null) {
            Optional<CartItem> existingItem = existingCart.getCartItems()
                    .stream()
                    .filter(item -> item.getId().equals(cartItemId))
                    .findFirst();

            if (existingItem.isPresent()) {

                cartItemRepository.deleteById(cartItemId);
                return true;

            } else {
                return false;
            }
        }
        return false;
    }

    public CartItem changeQuantity(Long userId, Long cartItemId, Integer qty) {

        Cart existingCart = cartRepository.findCartByUserId(userId);

        if (existingCart == null) {
            throw new RuntimeException("Cart not found");
        }

        Optional<CartItem> existingItem = existingCart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst();

        if (existingItem.isEmpty()) {
            throw new RuntimeException("Cart item not found");
        }

        CartItem item = existingItem.get();
        item.setQuantity(qty);

        return cartItemRepository.save(item);
    }
}
