package com.goodquestion.edutrek_server.modules.authentication.dto;

import com.goodquestion.edutrek_server.modules.authentication.entities.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicAccountDataDto {
    private String login;
    private List<Roles> roles;
}
