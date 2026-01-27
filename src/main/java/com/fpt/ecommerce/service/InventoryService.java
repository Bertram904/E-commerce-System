package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.ImportRequest;
import com.fpt.ecommerce.dto.request.SupplierRequest;
import com.fpt.ecommerce.dto.response.ImportInvoiceResponse;
import com.fpt.ecommerce.entity.*;
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
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

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

        // Initalize Invoice
        ImportInvoice invoice = importMapper.toImportInvoice(importRequest);
        invoice.setStaff(staff);
        invoice.setSupplier(supplier);

        invoice.setInvoiceCode("IMP_" + System.currentTimeMillis());

        List<ImportDetail> details = new ArrayList<>();
        double grandTotal = 0;

        for (ImportRequest.ImportItemRequest itemRequest : importRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            int newQuantity = product.getStockQuantity() + itemRequest.getQuantity();
            product.setStockQuantity(newQuantity);
            productRepository.save(product);

            ImportDetail detail = importMapper.toImportDetail(itemRequest);
            detail.setProduct(product);

            detail.setImportInvoice(invoice);

            details.add(detail);

            grandTotal += (itemRequest.getQuantity() * itemRequest.getImportPrice());
        }
        invoice.setDetails(details);
        invoice.setTotalAmount(grandTotal);

        ImportInvoice savedInvoice = importInvoiceRepository.save(invoice);

        return importMapper.toImportInvoiceResponse(savedInvoice);
    }

}
