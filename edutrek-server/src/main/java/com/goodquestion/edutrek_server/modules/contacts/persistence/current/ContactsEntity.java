package com.goodquestion.edutrek_server.modules.contacts.persistence.current;

import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(schema = "current", name = "contacts")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ContactsEntity extends AbstractContacts {

    public ContactsEntity(String contactName, String phone, String email, int statusId, int branchId, UUID targetCourseId, String comment) {
        super(contactName, phone, email, statusId, branchId, targetCourseId, comment);
    }

    public ContactsEntity(ContactsEntity contactEntity) {
        super(contactEntity);
    }

    public ContactsEntity(ContactsDataDto contactsDataDto) {
        super(contactsDataDto);
    }
}
