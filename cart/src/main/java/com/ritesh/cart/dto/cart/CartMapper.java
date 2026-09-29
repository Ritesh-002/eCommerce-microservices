package com.ritesh.cart.dto.cart;


import com.ritesh.cart.clients.ProductClient;
import com.ritesh.cart.dto.product.ProductResponseDTO;
import com.ritesh.cart.models.cart.Cart;
import com.ritesh.cart.models.cart.CartItem;
//import com.ritesh.cart.models.products.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final ProductClient productClient;

    public CartResponseDTO toCartResponseDTO(Cart cart) {

        CartResponseDTO response = new CartResponseDTO();
        response.setId(cart.getId());

        List<CartItemResponseDTO> itemResponses = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {

            ProductResponseDTO productResponseDTO = productClient.getProduct(item.getProductId());

            CartItemResponseDTO itemResponse = new CartItemResponseDTO();

            itemResponse.setCartItemId(item.getId());
            itemResponse.setProductId(item.getProductId());
            itemResponse.setProductName(productResponseDTO.getName());
            itemResponse.setPrice(productResponseDTO.getPrice());
            itemResponse.setQty(item.getQuantity());

            itemResponses.add(itemResponse);
        }

        response.setCartItems(itemResponses);

        return response;
    }

    public CartItemResponseDTO toCartItemResponseDTO(ProductResponseDTO productResponseDTO, CartItem cartItem) {
        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();

        cartItemResponseDTO.setCartItemId(cartItem.getId());
        cartItemResponseDTO.setProductId(cartItem.getProductId());
        cartItemResponseDTO.setProductName(productResponseDTO.getName());
        cartItemResponseDTO.setPrice(productResponseDTO.getPrice());
        cartItemResponseDTO.setQty(cartItem.getQuantity());

        return cartItemResponseDTO;
    }

    public CartItem toCartItemEntity(CartRequestDTO request, ProductResponseDTO product, Cart cart) {

        CartItem item = new CartItem();

        item.setProductId(request.getProductId());
        item.setCart(cart);
        item.setQuantity(request.getQty());

        return item;
    }
}