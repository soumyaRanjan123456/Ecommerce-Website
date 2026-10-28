package com.Product_Service.ProductService.service;

import com.Product_Service.ProductService.dto.ProductRequest;
import com.Product_Service.ProductService.dto.ProductResponse;
import com.Product_Service.ProductService.entity.Product;
import com.Product_Service.ProductService.exception.ResourceNotFoundException;
import com.Product_Service.ProductService.repository.ProductRepository;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger log =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest request) {

        log.info("Creating product: {}", request.getName());

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully. ID: {}", savedProduct.getId());

        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        log.info("Fetching all products");

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {

        log.info("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException(
                            "Product not found with ID: " + id);
                });

        return mapToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProductPrice(Long id, BigDecimal price) {

        log.info("Updating price for product ID: {}", id);

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Invalid price: {}", price);
            throw new IllegalArgumentException(
                    "Price must be greater than zero");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException(
                            "Product not found with ID: " + id);
                });

        product.setPrice(price);

        Product updatedProduct = productRepository.save(product);

        log.info("Product price updated successfully. ID: {}", id);

        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException(
                            "Product not found with ID: " + id);
                });

        productRepository.delete(product);

        log.info("Product deleted successfully. ID: {}", id);
    }

    private ProductResponse mapToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt()
        );
    }

}
