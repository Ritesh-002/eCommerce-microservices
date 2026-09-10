package com.ritesh.eCommerce.models.cart;

import com.ritesh.eCommerce.models.products.Product;
import com.ritesh.eCommerce.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_cart")
@Data
public class UserCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Product product;
    private User user;

}
