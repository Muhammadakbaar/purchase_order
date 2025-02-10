package com.backend.purchaseorder.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {
    private Integer id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 500, message = "Name cannot exceed 500 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Integer price;
    private Integer cost;

    @NotBlank(message = "Created by cannot be blank")
    private String createdBy;

    private String updatedBy;
}