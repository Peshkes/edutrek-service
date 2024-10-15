package com.goodquestion.edutrek_server.modules.contacts.persistence.archive;

import com.goodquestion.edutrek_server.modules.contacts.persistence.AbstractContacts;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;



@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "archive", name = "contacts")
public class ContactArchiveEntity extends AbstractContacts {

    @Column(name = "reason_of_archivation")
    private  String reasonOfArchivation;
    @Column(name = "archivation_date")
    private LocalDate archivationDate;

    public ContactArchiveEntity(ContactsEntity contactsEntity, String reasonOfArchivation) {
        super(contactsEntity);
        this.reasonOfArchivation = reasonOfArchivation;
        this.archivationDate = LocalDate.now();
    }


}
