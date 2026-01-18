package com.fpt.ecommerce.mapper;

import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.dto.response.MemberResponse;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.entity.Permission;
import com.fpt.ecommerce.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    // --- 1. Mapping REQUEST -> ENTITY ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "dob", source = "dob")
    Member toMember(RegisterRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToNames")
    @Mapping(target = "permissions", source = "roles", qualifiedByName = "mapRolesToPermissions")
    MemberResponse toMemberResponse(Member member);

    @Named("mapRolesToNames")
    default Set<String> mapRolesToNames(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Named("mapRolesToPermissions")
    default Set<String> mapRolesToPermissions(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        Set<String> permissions = new HashSet<>();

        for (Role role : roles) {
            if (role.getPermissions() != null) {
                for (Permission p : role.getPermissions()) {
                    permissions.add(p.getName());
                }
            }
        }
        return permissions;
    }
}