package com.ritesh.product.service.products;

import com.ritesh.product.dto.products.product.ProductMapper;
import com.ritesh.product.dto.products.product.ProductRequestDTO;
import com.ritesh.product.dto.products.product.ProductResponseDTO;
import com.ritesh.product.exceptions.InsufficientStockException;
import com.ritesh.product.exceptions.ProductNotFoundException;
import com.ritesh.product.models.products.Product;
import com.ritesh.product.repository.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponseDTO> fetchAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream().map(productMapper::toProductResponseDTO).toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with id " + id + " not found"
                        )
                );

        return productMapper.toProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO addProduct(ProductRequestDTO request) {
        Product product = productMapper.toProductEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDTO(savedProduct);
    }

    @Transactional
    public ProductResponseDTO editProduct(
            Long id,
            ProductRequestDTO request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with id " + id + " not found"
                        )
                );

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getQty() != null) {
            product.setQty(request.getQty());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        Product updatedProduct = productRepository.save(product);

        return productMapper.toProductResponseDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with id " + id + " not found"
                        )
                );

        productRepository.delete(product);
    }

    @Transactional
    public ProductResponseDTO reserveProductQuantity(
            Long productId,
            Integer quantity
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getQty() < quantity) {
            throw new InsufficientStockException("Insufficient stock available");
        }

        product.setQty(product.getQty() - quantity);

        return productMapper.toProductResponseDTO(product);
    }
}
