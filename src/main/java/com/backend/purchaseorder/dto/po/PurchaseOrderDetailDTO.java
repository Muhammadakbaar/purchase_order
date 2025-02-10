package com.backend.purchaseorder.dto.po;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDetailDTO {
    private Integer id;

    @NotNull(message = "PO Header ID cannot be null")
    private Integer pohId;

    @NotNull(message = "Item ID cannot be null")
    private Integer itemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer itemQty;

    @Min(value = 0, message = "Cost cannot be negative")
    private BigDecimal itemCost;

    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal itemPrice;

}

