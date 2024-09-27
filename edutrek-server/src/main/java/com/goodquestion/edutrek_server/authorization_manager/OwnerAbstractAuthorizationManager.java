package com.goodquestion.edutrek_server.authorization_manager;

import com.goodquestion.edutrek_server.modules.authentication.entities.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.repositories.AccountRepository;
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

    protected AccountDocument getAccountDocument(UUID accountId) {
        return accountRepository.findAccountDocumentByAccountId(accountId);
    }

    protected AuthorizationDecision checkOwnership(Supplier<Authentication> authenticationSupplier, UUID accountId) {
        Authentication authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        AccountDocument accountDocument = getAccountDocument(accountId);
        if (accountDocument != null) {
            String username = authentication.getName();
            boolean isOwner = accountDocument.getLogin().equals(username);

            return new AuthorizationDecision(isOwner);
        }

        return new AuthorizationDecision(false);
    }
}