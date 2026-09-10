package com.ritesh.eCommerce.controllers.products;

import com.ritesh.eCommerce.dto.products.product.ProductMapper;
import com.ritesh.eCommerce.dto.products.product.ProductRequestDTO;
import com.ritesh.eCommerce.dto.products.product.ProductResponseDTO;
import com.ritesh.eCommerce.models.products.Product;
import com.ritesh.eCommerce.services.products.ProductService;
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
        List<Product> allProducts = productService.fetchAllProducts();
        List<ProductResponseDTO> allProductResponseDTO = allProducts.stream().map(productMapper::toProductResponseDTO).toList();
        return new ResponseEntity<>(allProductResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return productService.getProduct(id)
                .map(p -> new ResponseEntity<>(productMapper.toProductResponseDTO(p), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toProductEntity(productRequestDTO);
        return productService.addProduct(product) ?
                ResponseEntity.ok("Product added successfully") :
                new ResponseEntity<>("Operation failed! Please try again", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequestDTO productRequestDTO, @PathVariable Long id) {
        Product product = productMapper.toProductEntity(productRequestDTO);
        return productService.editProduct(product, id) ? ResponseEntity.ok("Product updated successfully") : new ResponseEntity<>("Product failed to update", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable Long id) {
        return productService.deleteProduct(id) ? ResponseEntity.ok("Product deletion successful") : new ResponseEntity<>("Product failed to delete! please try again", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
