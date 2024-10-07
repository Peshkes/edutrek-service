package com.goodquestion.edutrek_server.modules.contacts.archive;




import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactsArchiveRepository extends MongoRepository<ContactArchiveDocument, UUID> {

}
