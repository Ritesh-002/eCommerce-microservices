package com.ritesh.eCommerce.services.products;

import com.ritesh.eCommerce.models.products.Product;
import com.ritesh.eCommerce.repository.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> fetchAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public boolean addProduct(Product product) {
        productRepository.save(product);
        return true;
    }

    public boolean editProduct(Product product, Long id) {
        Product productToEdit = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        if(product.getName() != null) productToEdit.setName(product.getName());
        if(product.getDescription() != null) productToEdit.setDescription(product.getDescription());
        if(product.getCategory() != null) productToEdit.setCategory(product.getCategory());
        if(product.getQty() != null) productToEdit.setQty(product.getQty());
        if(product.getPrice() != null) productToEdit.setPrice(product.getPrice());
        if(product.getBrand() != null) productToEdit.setBrand(product.getBrand());

        productRepository.save(productToEdit);
        return true;
    }

    public boolean deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> productRepository.deleteById(id));
        return true;
    }
}
