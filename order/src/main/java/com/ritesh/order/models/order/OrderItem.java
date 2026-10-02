package com.ritesh.order.models.order;

//import com.ritesh.order.models.products.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;

    @JsonProperty("id")
    private Long productId;

    @JsonProperty("name")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private BigDecimal priceAtCheckout;

    private Integer qty;

    private BigDecimal subTotal;

}
