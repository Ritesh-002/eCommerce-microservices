package com.ritesh.eCommerce.dto.products.product;

import com.ritesh.eCommerce.dto.products.productImage.ProductImageRequestDTO;
import com.ritesh.eCommerce.dto.products.productImage.ProductImageResponseDTO;
import com.ritesh.eCommerce.models.products.Product;
import com.ritesh.eCommerce.models.products.ProductImages;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {
    public ProductResponseDTO toProductResponseDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();

        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setBrand(product.getBrand());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setCategory(product.getCategory());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setSku(product.getSku());
        productResponseDTO.setQty(product.getQty());

        List<ProductImageResponseDTO> productImageResponses = new ArrayList<>();

        for(ProductImages productImage: product.getImages()) {
            ProductImageResponseDTO productImageResponseDTO = new ProductImageResponseDTO();
            productImageResponseDTO.setId(productImage.getId());
            productImageResponseDTO.setUrl(productImage.getUrl());

            productImageResponses.add(productImageResponseDTO);
        }

        productResponseDTO.setImages(productImageResponses);

        return productResponseDTO;
    }

    public Product toProductEntity(ProductRequestDTO productRequestDTO) {
        Product product = new Product();

        product.setName(productRequestDTO.getName());
        product.setBrand(productRequestDTO.getBrand());
        product.setDescription(productRequestDTO.getDescription());
        product.setCategory(productRequestDTO.getCategory());
        product.setPrice(productRequestDTO.getPrice());
        product.setSku(productRequestDTO.getSku());
        product.setQty(productRequestDTO.getQty());

        // arrays of image in productRequestDTO.getImages of type ProductImageRequestDTO;

        List<ProductImages> productImages = new ArrayList<>(); // needs id, url and product for each image

        for(ProductImageRequestDTO productImageRequestDTO: productRequestDTO.getImages()) {
            ProductImages productImage = new ProductImages();
            productImage.setUrl(productImageRequestDTO.getUrl());

            productImage.setProduct(product);
            productImages.add(productImage);
        }

        product.setImages(productImages);

        return product;
    }
}
