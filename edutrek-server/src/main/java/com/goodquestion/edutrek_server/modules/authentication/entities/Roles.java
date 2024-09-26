package com.goodquestion.edutrek_server.modules.authentication.entities;

import lombok.Getter;

@Getter
public enum Roles {
    ROLE_PRINCIPAL("ROLE_PRINCIPAL"),
    ROLE_MANAGER("ROLE_MANAGER");

    private final String value;

    Roles(String value) {
        this.value = value;
    }
}