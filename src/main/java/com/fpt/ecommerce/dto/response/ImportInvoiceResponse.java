package com.fpt.ecommerce.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImportInvoiceResponse {
    Long id;
    String invoiceCode;
    Double totalAmount;
    Date importDate;
    String staffName;
    String supplierName;
    List<ImportDetailResponse> details;
}
