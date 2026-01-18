package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.request.SupplierRequest;
import com.fpt.ecommerce.dto.response.SupplierResponse;
import com.fpt.ecommerce.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "importInvoices", ignore = true)
    Supplier toSupplier(SupplierRequest supplierRequest);


    SupplierResponse toSupplierResponse(Supplier supplier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "importInvoices", ignore = true)
    void updateSupplier(@MappingTarget Supplier supplier, SupplierRequest request);
}
