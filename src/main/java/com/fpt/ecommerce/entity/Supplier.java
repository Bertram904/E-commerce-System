package com.fpt.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="tblSupplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String contactPerson;
    private String phoneNumber;
    private String email;
    private String address;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<ImportInvoice> importInvoices;
}
