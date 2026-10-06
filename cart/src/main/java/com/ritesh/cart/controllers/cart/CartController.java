package com.ritesh.cart.controllers.cart;

import com.ritesh.cart.dto.cart.CartItemResponseDTO;
import com.ritesh.cart.dto.cart.CartRequestDTO;
import com.ritesh.cart.dto.cart.CartResponseDTO;
import com.ritesh.cart.dto.cart.UpdateCartItemRequestDTO;
import com.ritesh.cart.services.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getUserCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCart(userId)
        );
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        cartService.addToCart(
                                userId,
                                request
                        )
                );
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> updateQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequestDTO request) {

        return ResponseEntity.ok(
                cartService.changeQuantity(
                        userId,
                        cartItemId,
                        request.getQty()
                )
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {

        cartService.removeFromCart(
                userId,
                cartItemId
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @PathVariable Long userId) {

        cartService.deleteCart(userId);

        return ResponseEntity.noContent().build();
    }
}