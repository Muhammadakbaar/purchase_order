package com.backend.purchaseorder.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 500, message = "First name cannot exceed 500 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 500, message = "Last name cannot exceed 500 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Phone cannot be blank")
    private String phone;

    @NotBlank(message = "Created by cannot be blank")
    private String createdBy;

    @NotBlank(message = "Updated by cannot be blank")
    private String updatedBy;
}


