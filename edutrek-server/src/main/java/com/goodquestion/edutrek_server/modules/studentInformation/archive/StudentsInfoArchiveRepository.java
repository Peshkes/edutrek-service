package com.goodquestion.edutrek_server.modules.studentInformation.archive;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentsInfoArchiveRepository extends MongoRepository<StudentInfoArchiveDocument, UUID> {

}
