package com.goodquestion.edutrek_server.modules.contacts.persistence.archive;

import com.goodquestion.edutrek_server.modules.contacts.persistence.IContactRepository;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactsArchiveRepository   extends IContactRepository<ContactArchiveEntity>,JpaRepository<ContactArchiveEntity, UUID>, JpaSpecificationExecutor<ContactArchiveEntity> {

}
