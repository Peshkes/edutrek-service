package com.goodquestion.edutrek_server.modules.log.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepository extends MongoRepository<LogDocument, UUID> { }