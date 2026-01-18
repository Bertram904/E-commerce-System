package com.fpt.ecommerce.config;

import com.fpt.ecommerce.constant.PredefinedPermission; // Import hằng số
import com.fpt.ecommerce.entity.Permission;
import com.fpt.ecommerce.entity.Role;
import com.fpt.ecommerce.repository.PermissionRepository;
import com.fpt.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("STARTING DATA SEEDING...");

        Permission pViewProduct = createPermissionIfNotFound(PredefinedPermission.VIEW_PRODUCT, "Xem danh sách sản phẩm");
        Permission pViewProfile = createPermissionIfNotFound(PredefinedPermission.VIEW_PROFILE, "Xem thông tin cá nhân");

        Permission pCreateProduct = createPermissionIfNotFound(PredefinedPermission.CREATE_PRODUCT, "Tạo sản phẩm mới");
        Permission pUpdateProduct = createPermissionIfNotFound(PredefinedPermission.UPDATE_PRODUCT, "Cập nhật sản phẩm");
        Permission pDeleteProduct = createPermissionIfNotFound(PredefinedPermission.DELETE_PRODUCT, "Xóa sản phẩm");

        Permission pManageUsers = createPermissionIfNotFound(PredefinedPermission.MANAGE_USERS, "Quản lý người dùng (Full quyền)");

        //Supplier
        Permission pViewSupplier = createPermissionIfNotFound(PredefinedPermission.VIEW_SUPPLIER, "Xem danh sách nhà cung cấp");
        Permission pCreateSupplier = createPermissionIfNotFound(PredefinedPermission.CREATE_SUPPLIER, "Tạo nhà cung cấp mới");
        Permission pUpdateSupplier = createPermissionIfNotFound(PredefinedPermission.UPDATE_SUPPLIER, "Cập nhật nhà cung cấp");
        Permission pDeleteSupplier = createPermissionIfNotFound(PredefinedPermission.DELETE_SUPPLIER, "Xóa nhà cung cấp");

        createRoleIfNotFound(PredefinedPermission.ROLE_USER,
                new HashSet<>(Set.of(pViewProduct, pViewProfile)));

        createRoleIfNotFound(PredefinedPermission.ROLE_STAFF,
                new HashSet<>(Set.of(
                        pViewProfile,
                        pViewProduct, pCreateProduct, pUpdateProduct,
                        pViewSupplier, pCreateSupplier, pUpdateSupplier
                )));

        createRoleIfNotFound(PredefinedPermission.ROLE_ADMIN,
                new HashSet<>(Set.of(
                        pManageUsers, pViewProfile,
                        pViewProduct, pCreateProduct, pUpdateProduct, pDeleteProduct,
                        pViewSupplier, pCreateSupplier, pUpdateSupplier, pDeleteSupplier
                )));

        log.info("✅ DATA SEEDING COMPLETED.");
    }

    private Permission createPermissionIfNotFound(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    log.info("Creating permission: {}", name);
                    return permissionRepository.save(Permission.builder()
                            .name(name)
                            .description(description)
                            .build());
                });
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        Optional<Role> roleOptional = roleRepository.findByName(name);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            role.setPermissions(permissions);
            return roleRepository.save(role);
        } else {
            log.info("Creating role: {}", name);
            Role role = Role.builder()
                    .name(name)
                    .permissions(permissions)
                    .build();
            return roleRepository.save(role);
        }
    }
}