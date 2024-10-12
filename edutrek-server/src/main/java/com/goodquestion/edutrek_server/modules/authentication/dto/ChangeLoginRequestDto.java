package com.goodquestion.edutrek_server.modules.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.goodquestion.edutrek_server.error.ValidationErrors.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLoginRequestDto {
    @NotBlank(message = LOGIN_MANDATORY)
    @Size(min = 3, max = 50, message = LOGIN_SIZE)
    private String login;
}
