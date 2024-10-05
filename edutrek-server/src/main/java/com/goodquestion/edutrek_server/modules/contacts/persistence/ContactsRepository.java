package com.goodquestion.edutrek_server.modules.contacts.persistence;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactsRepository extends JpaRepository<ContactsEntity, UUID> , JpaSpecificationExecutor<ContactsEntity> {
    ContactsEntity findByPhoneOrEmail(String phone, String email);
}
