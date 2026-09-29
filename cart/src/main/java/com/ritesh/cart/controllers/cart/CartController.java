package com.ritesh.cart.controllers.cart;

import com.ritesh.cart.clients.ProductClient;
import com.ritesh.cart.dto.cart.*;
import com.ritesh.cart.models.cart.Cart;
import com.ritesh.cart.models.cart.CartItem;
import com.ritesh.cart.services.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/cart")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final ProductClient productClient;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getUserCart(@PathVariable Long userId) {
        Cart cart = cartService.getCart(userId);

        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toCartResponseDTO(cart));
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> addItemToCart(@PathVariable Long userId, @RequestBody CartRequestDTO cartRequestDTO) {
        CartItem cartItem = cartService.addToCart(userId, cartRequestDTO);

        if (cartItem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toCartItemResponseDTO(productClient.getProduct(cartItem.getProductId()), cartItem));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> updateQuantity(@PathVariable Long userId, @PathVariable Long cartItemId, @RequestBody UpdateCartItemRequestDTO requestDTO) {
        CartItem cartItem = cartService.changeQuantity(userId, cartItemId, requestDTO.getQty());

        if (cartItem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toCartItemResponseDTO(productClient.getProduct(cartItem.getProductId()), cartItem));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {

        if (!cartService.removeFromCart(userId, cartItemId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Cart item removed successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        return cartService.deleteCart(userId) ? new ResponseEntity<>("Cleared cart successfully", HttpStatus.OK) : new ResponseEntity<>("Unable to delete the cart", HttpStatus.NOT_FOUND);
    }
}
