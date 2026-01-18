package com.fpt.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceCode;
    private Double totalAmount;

    @CreationTimestamp
    private Date importDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Member staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "importInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImportDetail> details;
}
