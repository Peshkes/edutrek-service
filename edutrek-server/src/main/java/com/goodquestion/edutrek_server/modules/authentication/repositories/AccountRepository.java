package com.goodquestion.edutrek_server.modules.authentication.repositories;

import com.goodquestion.edutrek_server.modules.authentication.entities.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<AccountDocument, UUID> {
    AccountDocument findByLogin(String login);
    AccountDocument findByAccountId(UUID id);
    boolean existsById(UUID id);
    void deleteByAccountId(UUID id);
}