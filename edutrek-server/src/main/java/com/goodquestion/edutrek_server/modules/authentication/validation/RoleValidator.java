package com.goodquestion.edutrek_server.modules.authentication.validation;

import com.goodquestion.edutrek_server.modules.authentication.persistence.Roles;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;

public class RoleValidator implements ConstraintValidator<ValidRole, Roles> {
    @Override
    public boolean isValid(Roles role, ConstraintValidatorContext context) {
        return EnumSet.allOf(Roles.class).contains(role);
    }
}