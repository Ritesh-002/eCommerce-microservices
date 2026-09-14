package com.ritesh.eCommerce.controllers.cart;

import com.ritesh.eCommerce.models.cart.Cart;
import com.ritesh.eCommerce.models.cart.CartItem;
import com.ritesh.eCommerce.services.cart.CartService;
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
    public ResponseEntity<Cart> getUserCart(@PathVariable Long userId) {
        Cart cart = cartService.getCart(userId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<CartItem> addItemToCart(@PathVariable Long userId, @RequestBody Long productId, @RequestBody Integer qty) {
        CartItem cartItem = cartService.addToCart(userId, productId, qty);

        if (cartItem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartItem);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable Long userId, @PathVariable Long cartItemId, @RequestBody Integer qty) {
        CartItem cartItem = cartService.changeQuantity(userId, cartItemId, qty);

        if (cartItem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartItem);
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
