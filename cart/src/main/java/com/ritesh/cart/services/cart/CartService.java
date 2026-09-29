package com.ritesh.cart.services.cart;

import com.ritesh.cart.clients.ProductClient;
import com.ritesh.cart.dto.cart.CartMapper;
import com.ritesh.cart.dto.cart.CartRequestDTO;
import com.ritesh.cart.dto.product.ProductResponseDTO;
import com.ritesh.cart.models.cart.Cart;
import com.ritesh.cart.models.cart.CartItem;
//import com.ritesh.cart.models.products.Product;
//import com.ritesh.cart.models.users.User;
import com.ritesh.cart.repository.cart.CartItemRepository;
import com.ritesh.cart.repository.cart.CartRepository;
//import com.ritesh.cart.repository.products.ProductRepository;
//import com.ritesh.cart.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;

    private final ProductClient productClient;

    private final CartMapper cartMapper;

    public Cart getCart(Long userId) {
        return cartRepository.findCartByUserId(userId);
    }

    public CartItem addToCart(Long userId, CartRequestDTO cartRequestDTO) {

//        Product product = productRepository.findById(cartRequestDTO.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));

        System.out.println("Product ID from request = "
                + cartRequestDTO.getProductId());

        ProductResponseDTO product = productClient.getProduct(cartRequestDTO.getProductId());

        Cart existingCart = cartRepository.findCartByUserId(userId);

        CartItem cartItem;

        if (existingCart != null) {

            Optional<CartItem> existingItem = existingCart.getCartItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(cartRequestDTO.getProductId()))
                    .findFirst();

            if (existingItem.isPresent()) {

                cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + cartRequestDTO.getQty());

            } else {
                cartItem = cartMapper.toCartItemEntity(cartRequestDTO, product, existingCart);

                existingCart.getCartItems().add(cartItem);
            }
            cartRepository.save(existingCart);

        } else {

            Cart cart = new Cart();

//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));

            cart.setUserId(userId);
            cartItem = cartMapper.toCartItemEntity(cartRequestDTO, product, cart);

            cart.getCartItems().add(cartItem);

            cartRepository.save(cart);
        }

        return cartItem;
    }

    @Transactional
    public boolean deleteCart(Long userId) {
        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart != null) {
            cartRepository.delete(cart);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean removeFromCart(Long userId, Long cartItemId) {
        Cart existingCart = cartRepository.findCartByUserId(userId);
        if(existingCart != null) {
            Optional<CartItem> existingItem = existingCart.getCartItems()
                    .stream()
                    .filter(item -> item.getId().equals(cartItemId))
                    .findFirst();

            if (existingItem.isPresent()) {
                existingCart.getCartItems().remove(existingItem.get());
                cartItemRepository.deleteById(cartItemId);
                return true;

            } else {
                return false;
            }
        }
        return false;
    }

    @Transactional
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
