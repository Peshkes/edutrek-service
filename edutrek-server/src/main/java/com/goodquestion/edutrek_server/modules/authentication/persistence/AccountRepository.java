package com.goodquestion.edutrek_server.modules.authentication.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<AccountDocument, UUID> {
    AccountDocument findAccountDocumentByLogin(String login);
    AccountDocument findAccountDocumentByAccountId(UUID id);
    boolean existsAccountDocumentByLogin(String login);
    boolean existsAccountDocumentByAccountId(UUID id);
    AccountDocument deleteAccountDocumentByAccountId(UUID id);
}