package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.ProductRequest;
import com.fpt.ecommerce.dto.response.ProductResponse;
import com.fpt.ecommerce.entity.Category;
import com.fpt.ecommerce.entity.Product;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.ProductMapper;
import com.fpt.ecommerce.repository.CategoryRepository;
import com.fpt.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = productMapper.toProduct(productRequest);
        product.setCategory(category);

        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODCUT_NOT_FOUND));


    }
}
