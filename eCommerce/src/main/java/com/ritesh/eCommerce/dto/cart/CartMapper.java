package com.ritesh.eCommerce.dto.cart;


import com.ritesh.eCommerce.models.cart.Cart;
import com.ritesh.eCommerce.models.cart.CartItem;
import com.ritesh.eCommerce.models.products.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartMapper {

    public CartResponseDTO toCartResponseDTO(Cart cart) {

        CartResponseDTO response = new CartResponseDTO();
        response.setId(cart.getId());

        List<CartItemResponseDTO> itemResponses = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {

            CartItemResponseDTO itemResponse = new CartItemResponseDTO();

            itemResponse.setCartId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setPrice(item.getProduct().getPrice());
            itemResponse.setQty(item.getQuantity());

            itemResponses.add(itemResponse);
        }

        response.setCartItems(itemResponses);

        return response;
    }

    public CartItem toCartEntity(CartRequestDTO request, Product product, Cart cart) {

        CartItem item = new CartItem();

        item.setProduct(product);
        item.setCart(cart);
        item.setQuantity(request.getQty());

        return item;
    }
}