package com.goodquestion.edutrek_server.modules.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDataDto {
    private String login;
    private String password;
}
