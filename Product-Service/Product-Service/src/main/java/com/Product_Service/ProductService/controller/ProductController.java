package com.Product_Service.ProductService.controller;

import com.Product_Service.ProductService.dto.ProductRequest;
import com.Product_Service.ProductService.dto.ProductResponse;
import com.Product_Service.ProductService.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger log =
            LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create product
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            @Valid @RequestBody ProductRequest request) {

        log.info("Received request to create product: {}", request.getName());

        ProductResponse response = productService.addProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        log.info("Received request to fetch all products");

        List<ProductResponse> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id) {

        log.info("Received request to fetch product ID: {}", id);

        ProductResponse response = productService.getProductById(id);

        return ResponseEntity.ok(response);
    }

    // Update product price
    @   PatchMapping("/{id}/price")
    public ResponseEntity<ProductResponse> updateProductPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal price) {

        log.info("Received request to update price for product ID: {}", id);

        ProductResponse response =
                productService.updateProductPrice(id, price);

        return ResponseEntity.ok(response);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        log.info("Received request to delete product ID: {}", id);

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

}
