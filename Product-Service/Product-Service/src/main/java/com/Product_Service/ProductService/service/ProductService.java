package com.Product_Service.ProductService.service;

import com.Product_Service.ProductService.dto.ProductRequest;
import com.Product_Service.ProductService.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductResponse addProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProductPrice(Long id, BigDecimal price);

    void deleteProduct(Long id);
}
