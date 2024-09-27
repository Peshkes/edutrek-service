package com.goodquestion.edutrek_server.modules.authentication.dto;

import com.goodquestion.edutrek_server.modules.authentication.entities.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicAccountDataDto {
    private UUID id;
    private String email;
    private String login;
    private String name;
    private List<Roles> roles;
}
