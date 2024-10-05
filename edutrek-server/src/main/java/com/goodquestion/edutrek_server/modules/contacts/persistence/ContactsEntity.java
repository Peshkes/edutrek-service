package com.goodquestion.edutrek_server.modules.contacts.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "contacts")
@Getter
@NoArgsConstructor
public class ContactsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contact_id")
    private UUID contactId;
    @Setter
    @Column(name = "contact_name")
    private String contactName;
    @Setter
    @Column(name = "phone")
    private String phone;
    @Setter
    @Column(name = "email")
    private String email;

    @Setter
    @Column(name = "status_id")
    @JoinColumn(name = "status_id")
    private int statusId;
    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "target_course_id")
    private int targetCourseId;

    @Setter
    @Column(name = "comment")
    private String comment;

    public ContactsEntity(String contactName, String phone, String email, int statusId, int branchId, int targetCourseId, String comment) {
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.statusId = statusId;
        this.branchId = branchId;
        this.targetCourseId = targetCourseId;
        this.comment = comment;
    }
}
