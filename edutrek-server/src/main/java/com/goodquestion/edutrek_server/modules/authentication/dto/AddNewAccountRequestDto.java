package com.goodquestion.edutrek_server.modules.authentication.dto;

import static com.goodquestion.edutrek_server.error.ValidationErrors.*;

import com.goodquestion.edutrek_server.modules.authentication.persistence.Roles;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddNewAccountRequestDto {
    @NotNull(message = EMAIL_MANDATORY)
    @NotEmpty(message = EMAIL_NOT_EMPTY)
    @Email(message = EMAIL_INVALID_FORMAT)
    private String email;

    @NotNull(message = NAME_MANDATORY)
    @NotEmpty(message = NAME_NOT_EMPTY)
    @Size(min = 2, max = 50, message = NAME_SIZE)
    private String name;

    @NotEmpty(message = ROLES_MANDATORY)
    private List<@NotNull(message = ROLE_NOT_NULL) @Pattern(regexp = "^(PRINCIPAL|MANAGER)$", message = ROLE_INVALID) Roles> roles;
}