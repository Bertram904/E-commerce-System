package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.SupplierRequest;
import com.fpt.ecommerce.dto.response.SupplierResponse;
import com.fpt.ecommerce.entity.Supplier;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.SupplierMapper;
import com.fpt.ecommerce.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierService {
    SupplierRepository supplierRepository;
    SupplierMapper supplierMapper;

    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toSupplierResponse)
                .toList();
    }

    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Transactional
    public SupplierResponse createSupplier(SupplierRequest supplierRequest) {
        log.info("Creating supplier with emmail: {}", supplierRequest.getEmail());

        if (supplierRepository.existsByEmail(supplierRequest.getEmail())) {
            throw new AppException(ErrorCode.SUPPLIER_EMAIL_EXISTED, supplierRequest.getEmail());
        }

        if (supplierRepository.existsByPhoneNumber((supplierRequest.getPhoneNumber()))) {
            throw new AppException(ErrorCode.SUPPLIER_PHONE_EXISTED, supplierRequest.getPhoneNumber());
        }

        Supplier supplier = supplierMapper.toSupplier(supplierRequest);
        return supplierMapper.toSupplierResponse(supplierRepository.save(supplier));
    }
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier id: {}", id);

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND, id));

        // 1. Check trùng Email (Trừ chính nó)
        if (supplierRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new AppException(ErrorCode.SUPPLIER_EMAIL_EXISTED, request.getEmail());
        }
        // 2. Check trùng Phone (Trừ chính nó)
        if (supplierRepository.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), id)) {
            throw new AppException(ErrorCode.SUPPLIER_PHONE_EXISTED, request.getPhoneNumber());
        }

        supplierMapper.updateSupplier(existingSupplier, request);

        return supplierMapper.toSupplierResponse(supplierRepository.save(existingSupplier));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier id: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND, id));

        if (!supplier.getImportInvoices().isEmpty()) {
            throw new AppException(ErrorCode.SUPPLIER_HAS_INVOICES);
        }

        supplierRepository.delete(supplier);
    }
}
