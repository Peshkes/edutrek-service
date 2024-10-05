package com.goodquestion.edutrek_server.modules.ContactsArchive.persistence;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactsArchiveRepository extends MongoRepository<ContactArchiveEntity, UUID> {

}
