package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lecturers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class LecturerEntity {
    @Id
    @Column(name = "lecturer_id")
    private UUID lecturerId;
    @Setter
    @Column(name = "lecturer_name")
    private String lecturerName;
    @Setter
    @Column(name = "phone")
    private String phone;
    @Setter
    @Column(name = "email")
    private String email;
    @Setter
    @Column(name = "branch_id")
    private int branchId;
    @Setter
    @Column(name = "comment")
    private String comment;

    public LecturerEntity(String lecturerName, String phone, String email, int branchId, String comment) {
        this.lecturerId = UUID.randomUUID();
        this.lecturerName = lecturerName;
        this.phone = phone;
        this.email = email;
        this.branchId = branchId;
        this.comment = comment;
    }

    public LecturerEntity(LecturerEntity entity) {
        this.lecturerId = entity.getLecturerId();
        this.lecturerName = entity.getLecturerName();
        this.phone = entity.getPhone();
        this.email = entity.getEmail();
        this.branchId = entity.getBranchId();
        this.comment = entity.getComment();
    }
}
