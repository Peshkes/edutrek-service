package com.goodquestion.edutrek_server.modules.contacts.persistence;

import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsEntity;
import com.goodquestion.edutrek_server.modules.lecturer.persistence.BaseLecturer;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface IContactRepository<T extends AbstractContacts> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
   Optional <AbstractContacts> getByContactId(UUID id);

   boolean existsByPhoneOrEmail( String phone, String email);
}
