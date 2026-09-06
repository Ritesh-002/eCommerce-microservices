package com.ritesh.eCommerce.repository.products;

import com.ritesh.eCommerce.models.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
