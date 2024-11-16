package com.goodquestion.edutrek_server.authorization_manager;

import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;


@Slf4j
public abstract class OwnerAbstractAuthorizationManager {

    protected final AccountRepository accountRepository;

    public OwnerAbstractAuthorizationManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    protected AuthorizationDecision checkOwnership(Supplier<Authentication> authenticationSupplier, Object identifier) {
        Authentication authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        String username = authentication.getName();
        AccountDocument accountDocument = null;

        if (identifier instanceof UUID accountId) {
            accountDocument = accountRepository.findById(accountId).orElse(null);
        } else if (identifier instanceof String login) {
            Optional<AccountDocument> accountOptional = accountRepository.findAccountDocumentByLogin(login);
            if (accountOptional.isPresent()) {
                accountDocument = accountOptional.get();
            }
        }

        if (accountDocument != null) {
            boolean isOwner = accountDocument.getLogin().equals(username);
            return new AuthorizationDecision(isOwner);
        }

        return new AuthorizationDecision(false);
    }
}