package com.goodquestion.edutrek_server.modules.contacts.persistence;

import com.goodquestion.edutrek_server.modules.contacts.dto.ContactsDataDto;
import com.goodquestion.edutrek_server.modules.contacts.persistence.current.ContactsEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class AbstractContacts {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contact_id")
    private UUID contactId;
    @Column(name = "contact_name")
    private String contactName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "status_id")
    private int statusId;
    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "target_course_id")
    private UUID targetCourseId;
    @Column(name = "comment")
    private String comment;

    public AbstractContacts(String contactName, String phone, String email, int statusId, int branchId, UUID targetCourseId, String comment) {
        this.contactId = UUID.randomUUID();
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.statusId = statusId;
        this.branchId = branchId;
        this.targetCourseId = targetCourseId;
        this.comment = comment;
    }

    public AbstractContacts(ContactsEntity contactEntity) {
        this.contactId = contactEntity.getContactId();
        this.contactName = contactEntity.getContactName();
        this.phone = contactEntity.getPhone();
        this.email = contactEntity.getEmail();
        this.statusId = contactEntity.getStatusId();
        this.branchId = contactEntity.getBranchId();
        this.targetCourseId = contactEntity.getTargetCourseId();
        this.comment = contactEntity.getComment();
    }

    public AbstractContacts(ContactsDataDto contactsDataDto) {
        this.contactId = UUID.randomUUID();
        this.contactName = contactsDataDto.getContactName();
        this.phone = contactsDataDto.getPhone();
        this.email = contactsDataDto.getEmail();
        this.statusId = contactsDataDto.getStatusId();
        this.branchId = contactsDataDto.getBranchId();
        this.targetCourseId = contactsDataDto.getTargetCourseId();
        this.comment = contactsDataDto.getComment();
    }
}
