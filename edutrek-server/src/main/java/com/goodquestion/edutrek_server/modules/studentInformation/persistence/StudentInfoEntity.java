package com.goodquestion.edutrek_server.modules.studentInformation.persistence;


import com.goodquestion.edutrek_server.modules.contacts.persistence.ContactsEntity;
import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;


@Entity
@Table(name = "students_information")
@Getter
@Setter
@NoArgsConstructor
public class StudentInfoEntity extends ContactsEntity {

    @Column(name = "contact_id")
    private UUID contactId;

    @Column(name = "full_payment")
    private int fullPayment;

    @Column(name = "documents_done")
    private boolean documentsDone;

    //    @Id
//    @OneToOne
//    @JoinColumn(name="contact_id", referencedColumnName = "contact_id")
//    private ContactsEntity contact;

    public StudentInfoEntity(UUID contactId, String contactName, String phone, String email, int statusId, int branchId, UUID targetCourseId, String comment, int fullPayment, boolean documentsDone) {
        super(contactName, phone, email, statusId, branchId, targetCourseId, comment);
        this.contactId = contactId;
        this.fullPayment = fullPayment;
        this.documentsDone = documentsDone;
    }

    public StudentInfoEntity(UUID contactId, int fullPayment, boolean documentsDone) {
        this.contactId = contactId;
        this.fullPayment = fullPayment;
        this.documentsDone = documentsDone;
    }

}
