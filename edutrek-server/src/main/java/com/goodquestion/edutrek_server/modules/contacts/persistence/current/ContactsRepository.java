package com.goodquestion.edutrek_server.modules.contacts.persistence.current;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface ContactsRepository extends JpaRepository<ContactsEntity, UUID> , JpaSpecificationExecutor<ContactsEntity> {
    ContactsEntity findByPhoneOrEmail(String phone, String email);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM current.contacts WHERE contact_id = :id", nativeQuery = true)
    void deleteContactById(UUID id);
}
