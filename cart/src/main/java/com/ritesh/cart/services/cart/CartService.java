package com.ritesh.cart.services.cart;

import com.ritesh.cart.clients.ProductClient;
import com.ritesh.cart.clients.UserClient;
import com.ritesh.cart.dto.cart.CartMapper;
import com.ritesh.cart.dto.cart.CartRequestDTO;
import com.ritesh.cart.dto.cart.CartResponseDTO;
import com.ritesh.cart.dto.cart.CartItemResponseDTO;
import com.ritesh.cart.dto.product.ProductResponseDTO;
import com.ritesh.cart.exceptions.AccessDeniedException;
import com.ritesh.cart.exceptions.CartItemNotFoundException;
import com.ritesh.cart.exceptions.CartNotFoundException;
import com.ritesh.cart.exceptions.ProductNotFoundException;
import com.ritesh.cart.models.cart.Cart;
import com.ritesh.cart.models.cart.CartItem;
import com.ritesh.cart.repository.cart.CartItemRepository;
import com.ritesh.cart.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final ProductClient productClient;
    private final UserClient userClient;
    private final CartMapper cartMapper;

    public CartResponseDTO getCart(Long userId, Jwt jwt) {

        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException(
                    "Cart not found for user with id " + userId
            );
        }

        // Get the cart owner's Keycloak ID from User Service.
        String cartOwnerKeycloakId =
                userClient.getUserKeycloakId(userId, jwt);

        // Ensure the authenticated customer owns this cart.
        if (!jwt.getSubject().equals(cartOwnerKeycloakId)) {
            throw new AccessDeniedException(
                    "You are not allowed to access this cart"
            );
        }

        return cartMapper.toCartResponseDTO(cart, jwt);
    }

    @Transactional
    public CartItemResponseDTO addToCart(
            Long userId,
            CartRequestDTO request,
            Jwt jwt) {

        ProductResponseDTO product =
                productClient.getProduct(request.getProductId(), jwt);

        if (product == null) {
            throw new ProductNotFoundException(
                    "Product with id " + request.getProductId() + " not found"
            );
        }

        Cart existingCart =
                cartRepository.findCartByUserId(userId);

        CartItem cartItem;

        if (existingCart != null) {

            cartItem = existingCart.getCartItems()
                    .stream()
                    .filter(item ->
                            item.getProductId()
                                    .equals(request.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {

                cartItem.setQuantity(
                        cartItem.getQuantity() + request.getQty()
                );

            } else {

                cartItem = cartMapper.toCartItemEntity(
                        request,
                        product,
                        existingCart
                );

                existingCart.getCartItems().add(cartItem);
            }

            cartRepository.save(existingCart);

        } else {

            Cart cart = new Cart();
            cart.setUserId(userId);

            cartItem = cartMapper.toCartItemEntity(
                    request,
                    product,
                    cart
            );

            cart.getCartItems().add(cartItem);

            cartRepository.save(cart);
        }

        return cartMapper.toCartItemResponseDTO(
                product,
                cartItem
        );
    }

    @Transactional
    public void deleteCart(Long userId, Jwt jwt) {
        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException(
                    "Cart not found for user with id " + userId
            );
        }

        cartRepository.delete(cart);
    }

    @Transactional
    public void removeFromCart(
            Long userId,
            Long cartItemId,
            Jwt jwt) {

        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException(
                    "Cart not found for user with id " + userId
            );
        }

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item ->
                        item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "Cart item with id " + cartItemId +
                                        " not found in user's cart"
                        )
                );

        cart.getCartItems().remove(cartItem);

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public CartItemResponseDTO changeQuantity(
            Long userId,
            Long cartItemId,
            Integer qty,
            Jwt jwt) {

        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException(
                    "Cart not found for user with id " + userId
            );
        }

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item ->
                        item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "Cart item with id " + cartItemId +
                                        " not found in user's cart"
                        )
                );

        cartItem.setQuantity(qty);

        CartItem updatedItem =
                cartItemRepository.save(cartItem);

        ProductResponseDTO product =
                productClient.getProduct(
                        updatedItem.getProductId(),
                        jwt
                );

        return cartMapper.toCartItemResponseDTO(
                product,
                updatedItem
        );
    }
}