package com.fpt.ecommerce.repository;

import com.fpt.ecommerce.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
