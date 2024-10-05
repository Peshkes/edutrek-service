package com.goodquestion.edutrek_server.modules.lecturer.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lecturers")
@Getter
@NoArgsConstructor
public class LecturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private UUID branchId;
    @Setter
    @Column(name = "comment")
    private String comment;

    public LecturerEntity(String lecturerName, String phone, String email, UUID branchId, String comment) {
        this.lecturerName = lecturerName;
        this.phone = phone;
        this.email = email;
        this.branchId = branchId;
        this.comment = comment;
    }
}
