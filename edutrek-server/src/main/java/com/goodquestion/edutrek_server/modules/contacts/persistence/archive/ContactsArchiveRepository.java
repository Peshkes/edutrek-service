package com.goodquestion.edutrek_server.modules.contacts.persistence.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactsArchiveRepository  extends JpaRepository<ContactArchiveEntity, UUID>, JpaSpecificationExecutor<ContactArchiveEntity> {

}
