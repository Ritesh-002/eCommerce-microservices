package com.ritesh.eCommerce.dto.cart;

import com.ritesh.eCommerce.models.cart.CartItem;
import com.ritesh.eCommerce.models.products.Product;
import com.ritesh.eCommerce.models.users.User;
import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {
    private Long id;
    private List<CartItemResponseDTO> cartItems;
}
