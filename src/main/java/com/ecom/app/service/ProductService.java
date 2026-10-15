package com.ecom.app.service;

import com.ecom.app.dto.ProductRequest;
import com.ecom.app.dto.ProductResponse;
import com.ecom.app.model.Product;
import com.ecom.app.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){

        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts(){

        return productRepository.findByActiveTrue().stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    public boolean deleteProduct(long id){
            return productRepository.findById(id)
                    .map(product->{
                        product.setActive(false);
                        productRepository.save(product);
                        return true;
                    }).orElse(false);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest updateProduct){

        return productRepository.findById(id).map(existingProduct ->{
            updateProductFromRequest(existingProduct,updateProduct);
            Product savedProduct = productRepository.save(existingProduct);
            return mapToProductResponse(savedProduct);
        });
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    private ProductResponse mapToProductResponse(Product savedProduct){

        ProductResponse response = new ProductResponse();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setStockQuantity(savedProduct.getStockQuantity());
        response.setCategory(savedProduct.getCategory());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setActive(savedProduct.getActive());
        return response;
    }

    public List<ProductResponse> getProducts(String keyword){

        return productRepository.searchProducts(keyword)
                .stream()
                .map(this :: mapToProductResponse)
                .collect(Collectors.toList());
    }
}
