package com.goodquestion.edutrek_server.authorization_manager;

import com.goodquestion.edutrek_server.modules.authentication.repositories.AccountRepository;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

@Component
public class OwnerAuthorizationManager extends OwnerAbstractAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    public OwnerAuthorizationManager(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        String pathVariableId = context.getVariables().get("id");
        UUID accountId = UUID.fromString(pathVariableId);
        return checkOwnership(authenticationSupplier, accountId);
    }

}