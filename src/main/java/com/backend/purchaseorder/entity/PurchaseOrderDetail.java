package com.backend.purchaseorder.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "po_d")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "poh_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private PurchaseOrderHeader purchaseOrderHeader;

    @NotNull(message = "Item ID cannot be null")
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_qty")
    private Integer itemQty;

    @Column(name = "item_cost")
    private BigDecimal itemCost;

    @Column(name = "item_price")
    private BigDecimal itemPrice;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;
}


