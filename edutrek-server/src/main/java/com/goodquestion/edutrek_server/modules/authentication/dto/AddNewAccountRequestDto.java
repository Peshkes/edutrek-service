package com.goodquestion.edutrek_server.modules.authentication.dto;

import com.goodquestion.edutrek_server.modules.authentication.persistence.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddNewAccountRequestDto {
    @NotNull(message = "Email is mandatory")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Name is mandatory")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotEmpty(message = "Roles are mandatory")
    private List<@NotNull(message = "Role cannot be null") Roles> roles;
}