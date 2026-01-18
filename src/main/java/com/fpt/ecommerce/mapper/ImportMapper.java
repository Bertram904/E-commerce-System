package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.request.ImportRequest;
import com.fpt.ecommerce.dto.response.ImportDetailResponse;
import com.fpt.ecommerce.dto.response.ImportInvoiceResponse;
import com.fpt.ecommerce.entity.ImportDetail;
import com.fpt.ecommerce.entity.ImportInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImportMapper {

    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    ImportInvoice toImportInvoice(ImportRequest importRequest);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "importInvoice", ignore = true)
    ImportDetail toImportDetail(ImportRequest.ImportItemRequest itemRequest);

    @Mapping(target = "staffName", source = "staff.username")
    @Mapping(target = "supplierName", source = "supplier.name")
    ImportInvoiceResponse toImportInvoiceResponse(ImportInvoice importInvoice);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ImportDetailResponse toImportDetailResponse(ImportDetail entity);
}
