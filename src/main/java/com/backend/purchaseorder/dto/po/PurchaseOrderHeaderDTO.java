package com.backend.purchaseorder.dto.po;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderHeaderDTO {
    private Integer id;
    private LocalDateTime datetime;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private BigDecimal totalPrice;
    private BigDecimal totalCost;
    private String createdBy;
    private String updatedBy;
    private List<PurchaseOrderDetailDTO> poDetails;
}

