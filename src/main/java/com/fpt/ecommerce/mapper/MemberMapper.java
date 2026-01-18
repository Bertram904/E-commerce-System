package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    Member toMember(RegisterRequest request);
}
