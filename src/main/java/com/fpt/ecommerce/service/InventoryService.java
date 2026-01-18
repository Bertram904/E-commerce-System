package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.ImportRequest;
import com.fpt.ecommerce.dto.response.ImportInvoiceResponse;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.entity.Supplier;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.ImportMapper;
import com.fpt.ecommerce.repository.ImportInvoiceRepository;
import com.fpt.ecommerce.repository.MemberRepository;
import com.fpt.ecommerce.repository.ProductRepository;
import com.fpt.ecommerce.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryService {
    ProductRepository productRepository;
    ImportInvoiceRepository importInvoiceRepository;
    MemberRepository memberRepository;
    SupplierRepository supplierRepository;
    ImportMapper importMapper;

    @Transactional
    public ImportInvoiceResponse importGoods(ImportRequest importRequest, String staffUsername) {
        Member staff = memberRepository.findByUsername(staffUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Supplier supplier = supplierRepository.findById(importRequest.getSupplierId())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));

    }
}
