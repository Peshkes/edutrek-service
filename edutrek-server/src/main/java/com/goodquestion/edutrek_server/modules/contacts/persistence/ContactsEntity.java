package com.goodquestion.edutrek_server.modules.contacts.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "contacts")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public class ContactsEntity {
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
    @JoinColumn(name = "status_id")
    private int statusId;
    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "target_course_id")
    private UUID targetCourseId;


    @Column(name = "comment")
    private String comment;

    public ContactsEntity(String contactName, String phone, String email, int statusId, int branchId, UUID targetCourseId, String comment) {
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.statusId = statusId;
        this.branchId = branchId;
        this.targetCourseId = targetCourseId;
        this.comment = comment;
    }
}
