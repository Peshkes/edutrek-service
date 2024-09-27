package com.goodquestion.edutrek_server.modules.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLoginRequestDto {
    @NotBlank(message = "Login is mandatory")
    @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    private String login;
}
