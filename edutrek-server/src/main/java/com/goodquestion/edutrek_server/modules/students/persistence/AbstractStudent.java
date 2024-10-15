package com.goodquestion.edutrek_server.modules.students.persistence;

import com.goodquestion.edutrek_server.modules.students.dto.StudentsDataDto;
import com.goodquestion.edutrek_server.modules.students.persistence.current.StudentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractStudent {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "student_id")
    private UUID studentId;
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
    @Column(name = "full_payment")
    private int fullPayment;
    @Column(name = "documents_done")
    private boolean documentsDone;

    public AbstractStudent(String contactName, String phone, String email, int statusId, int branchId, UUID targetCourseId, String comment, int fullPayment, boolean documentsDone) {
        this.studentId = UUID.randomUUID();
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.statusId = statusId;
        this.branchId = branchId;
        this.targetCourseId = targetCourseId;
        this.comment = comment;
        this.fullPayment = fullPayment;
        this.documentsDone = documentsDone;
    }

    public AbstractStudent(StudentsDataDto studentsDataDto) {
        this.studentId = UUID.randomUUID();
        this.contactName = studentsDataDto.getContactName();
        this.phone = studentsDataDto.getPhone();
        this.email = studentsDataDto.getEmail();
        this.statusId = studentsDataDto.getStatusId();
        this.branchId = studentsDataDto.getBranchId();
        this.targetCourseId = studentsDataDto.getTargetCourseId();
        this.comment = studentsDataDto.getComment();
        this.fullPayment = studentsDataDto.getFullPayment();
        this.documentsDone = studentsDataDto.isDocumentsDone();
    }

    public AbstractStudent(StudentEntity studentEntity) {
        this.studentId = studentEntity.getStudentId();
        this.contactName = studentEntity.getContactName();
        this.phone = studentEntity.getPhone();
        this.email = studentEntity.getEmail();
        this.statusId = studentEntity.getStatusId();
        this.branchId = studentEntity.getBranchId();
        this.targetCourseId = studentEntity.getTargetCourseId();
        this.comment = studentEntity.getComment();
        this.fullPayment = studentEntity.getFullPayment();
        this.documentsDone = studentEntity.isDocumentsDone();
    }
    
}
