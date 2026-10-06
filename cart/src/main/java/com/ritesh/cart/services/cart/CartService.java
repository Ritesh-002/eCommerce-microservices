package com.ritesh.cart.services.cart;

import com.ritesh.cart.clients.ProductClient;
import com.ritesh.cart.dto.cart.CartMapper;
import com.ritesh.cart.dto.cart.CartRequestDTO;
import com.ritesh.cart.dto.cart.CartResponseDTO;
import com.ritesh.cart.dto.cart.CartItemResponseDTO;
import com.ritesh.cart.dto.product.ProductResponseDTO;
import com.ritesh.cart.models.cart.Cart;
import com.ritesh.cart.models.cart.CartItem;
import com.ritesh.cart.repository.cart.CartItemRepository;
import com.ritesh.cart.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final ProductClient productClient;
    private final CartMapper cartMapper;

    public CartResponseDTO getCart(Long userId) {

        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart == null) {
            return null;
        }

        return cartMapper.toCartResponseDTO(cart);
    }

    @Transactional
    public CartItemResponseDTO addToCart(
            Long userId,
            CartRequestDTO request) {

        ProductResponseDTO product =
                productClient.getProduct(request.getProductId());

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
    public void deleteCart(Long userId) {

        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart != null) {
            cartRepository.delete(cart);
        }
    }

    @Transactional
    public void removeFromCart(
            Long userId,
            Long cartItemId) {

        Cart cart =
                cartRepository.findCartByUserId(userId);

        if (cart != null) {

            CartItem cartItem = cart.getCartItems()
                    .stream()
                    .filter(item ->
                            item.getId().equals(cartItemId))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cart.getCartItems().remove(cartItem);
                cartItemRepository.deleteById(cartItemId);
            }
        }
    }

    @Transactional
    public CartItemResponseDTO changeQuantity(
            Long userId,
            Long cartItemId,
            Integer qty) {

        Cart cart =
                cartRepository.findCartByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item ->
                        item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found")
                );

        cartItem.setQuantity(qty);

        CartItem updatedItem =
                cartItemRepository.save(cartItem);

        ProductResponseDTO product =
                productClient.getProduct(
                        updatedItem.getProductId()
                );

        return cartMapper.toCartItemResponseDTO(
                product,
                updatedItem
        );
    }
}