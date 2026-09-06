package com.ritesh.eCommerce.models.products;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ritesh.eCommerce.models.users.User;
import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
