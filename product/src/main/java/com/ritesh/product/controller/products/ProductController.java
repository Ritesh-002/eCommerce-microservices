package com.ritesh.product.controller.products;

import com.ritesh.product.dto.products.product.ProductMapper;
import com.ritesh.product.dto.products.product.ProductRequestDTO;
import com.ritesh.product.dto.products.product.ProductResponseDTO;
import com.ritesh.product.dto.products.product.ReserveStockRequestDTO;
import com.ritesh.product.service.products.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.fetchAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO response = productService.addProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ProductResponseDTO> reserveStock(
            @PathVariable Long id,
            @Valid @RequestBody ReserveStockRequestDTO request) {

        ProductResponseDTO product =
                productService.reserveProductQuantity(
                        id,
                        request.getQty()
                );

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO response =
                productService.editProduct(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
