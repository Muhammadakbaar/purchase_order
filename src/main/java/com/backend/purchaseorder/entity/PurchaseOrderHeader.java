package com.backend.purchaseorder.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "po_h")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderHeader {

    @Id
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "created_by")
    private String createdBy;

    @NotNull
    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @NotNull
    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @NotNull
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

    @OneToMany(mappedBy = "purchaseOrderHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PurchaseOrderDetail> details;
}



