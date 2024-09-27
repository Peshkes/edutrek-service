package com.goodquestion.edutrek_server.modules.authentication.entities;

import lombok.Getter;

@Getter
public enum Roles {
    ROLE_PRINCIPAL("ROLE_PRINCIPAL", "PRINCIPAL"),
    ROLE_MANAGER("ROLE_MANAGER", "MANAGER");

    private final String value;
    private final String shortValue;

    Roles(String value, String shortValue) {
        this.value = value;
        this.shortValue = shortValue;
    }
}