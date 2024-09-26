package com.goodquestion.edutrek_server.modules.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResultDto {
    private String accessToken;
    private String refreshToken;
}
