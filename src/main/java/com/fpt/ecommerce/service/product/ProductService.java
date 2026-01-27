package com.fpt.ecommerce.service.product;

import com.fpt.ecommerce.dto.request.ProductRequest;
import com.fpt.ecommerce.dto.response.ProductResponse;
import com.fpt.ecommerce.entity.Category;
import com.fpt.ecommerce.entity.Product;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.ProductMapper;
import com.fpt.ecommerce.repository.CategoryRepository;
import com.fpt.ecommerce.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // 1. Luôn có Log để debug
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;

    // 1. GET ALL
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    // 2. GET BY ID (Bổ sung thêm cái này vì rất cần thiết)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    // 3. CREATE
    @Transactional // 2. Đảm bảo toàn vẹn dữ liệu
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());

        // Tìm Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Map DTO -> Entity
        Product product = productMapper.toProduct(request);
        product.setCategory(category);

        // Save & Return
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    // 4. UPDATE
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product id: {}", id);

        // Tìm Product cũ
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Tìm Category mới (nếu user đổi danh mục)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Update thông tin (Sử dụng MapStruct @MappingTarget)
        productMapper.updateProduct(product, request);

        // Update category thủ công (vì Mapper thường ignore quan hệ)
        product.setCategory(category);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    // 5. DELETE
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product id: {}", id);

        if (!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // Vì Entity đã có @SQLDelete, lệnh này sẽ update deleted = true (Soft Delete)
        productRepository.deleteById(id);
    }
}