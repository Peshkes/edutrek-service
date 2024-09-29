package com.goodquestion.edutrek_server.authorization_manager;

import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;

import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public abstract class OwnerAbstractAuthorizationManager {

    protected final AccountRepository accountRepository;

    public OwnerAbstractAuthorizationManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    protected AuthorizationDecision checkOwnership(Supplier<Authentication> authenticationSupplier, UUID accountId) {
        Authentication authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        AccountDocument accountDocument = accountRepository.findById(accountId).orElse(null);
        if (accountDocument != null) {
            String username = authentication.getName();
            boolean isOwner = accountDocument.getLogin().equals(username);

            return new AuthorizationDecision(isOwner);
        }

        return new AuthorizationDecision(false);
    }
}